/**
 * Copyright (C) 2010-2016 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.webp.intro.spring.testing.coverage.instrumentation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

/*
    Not: ASM kaynak testlerinden kopyala yapıştır bir sınıftır.
*/

/**
 * A ClassWriter that computes the common super class of two classes without
 * actually loading them with a ClassLoader.
 *
 * @author Eric Bruneton
 */
public class ComputeClassWriter extends ClassWriter {

	private ClassLoader l = getClass().getClassLoader();
	
    public ComputeClassWriter(final int flags) {
        super(flags);
    }
    
    @Override
    protected String getCommonSuperClass(final String type1, final String type2) {
        try {
        	ClassReader info1;
        	ClassReader info2;
        	try {
        		info1 = typeInfo(type1);
            } catch (NullPointerException e) {
            	// Eğer sınıf bulunamazsa olabilir
                throw new RuntimeException("Class not found: "+type1+": "+e.toString(), e);
        	}
        	try {
        		info2 = typeInfo(type2);
            } catch (NullPointerException e) {
                // Eğer sınıf bulunamazsa olabilir
                throw new RuntimeException("Class not found: "+type2+": "+e.toString(), e);
        	}

            if ((info1.getAccess() & Opcodes.ACC_INTERFACE) != 0) {
                if (typeImplements(type2, info2, type1)) {
                    return type1;
                }
                if ((info2.getAccess() & Opcodes.ACC_INTERFACE) != 0) {
                    if (typeImplements(type1, info1, type2)) {
                        return type2;
                    }
                }
                return "java/lang/Object";
            }
            if ((info2.getAccess() & Opcodes.ACC_INTERFACE) != 0) {
                if (typeImplements(type1, info1, type2)) {
                    return type2;
                } else {
                    return "java/lang/Object";
                }
            }
            StringBuilder b1 = typeAncestors(type1, info1);
            StringBuilder b2 = typeAncestors(type2, info2);
            String result = "java/lang/Object";
            int end1 = b1.length();
            int end2 = b2.length();
            while (true) {
                int start1 = b1.lastIndexOf(";", end1 - 1);
                int start2 = b2.lastIndexOf(";", end2 - 1);
                if (start1 != -1 && start2 != -1
                        && end1 - start1 == end2 - start2) {
                    String p1 = b1.substring(start1 + 1, end1);
                    String p2 = b2.substring(start2 + 1, end2);
                    if (p1.equals(p2)) {
                        result = p1;
                        end1 = start1;
                        end2 = start2;
                    } else {
                        return result;
                    }
                } else {
                    return result;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        } catch (NullPointerException e) {
            // Eğer sınıf bulunamazsa olabilir
            throw new RuntimeException(e.toString());
        }
    }
    
    /**
     * Verilen tipin ata sınıflarının dahili isimlerini döner
     *
     * @param type
     *            sınıf veya interface'in dahili adı.
     * @param info
     *            'type' dan sorumlu ClassReader.
     * @return ';' ile ayrılmış ata sınıfların 'type''larını içeren StringBuilder.
     *          Dönen string şu formattadır:
     *         ";type1;type2 ... ;typeN", type1, 'type' iken typeN ise nesnenin doğrudan
     *         alt sınıfıdır. Eğer 'type' nesne ise dönecek olan String boştur
     * @throws IOException
     *             type'ın veya başka ata sınıfın byte kodu yüklenemezse
     */
    private StringBuilder typeAncestors(String type, ClassReader info)
            throws IOException {
        StringBuilder b = new StringBuilder();
        while (!"java/lang/Object".equals(type)) {
            b.append(';').append(type);
            type = info.getSuperName();
            info = typeInfo(type);
        }
        return b;
    }

    /**
     * Verilen type verilen interface'i kullanıyorsa true döner
     *
     * @param type
     *            sınıf veya interface'in dahili adı.
     * @param info
     *            'type' dan sorumlu ClassReader.
     * @param itf
     *            interface'in dahili adı.
     * @return eğer type doğrudan veya dolaylı olarak itf'i implement ediyorsa true döner
     * @throws IOException
     *             type'ın veya başka ata sınıfın byte kodu yüklenemezse
     */
    private boolean typeImplements(String type, ClassReader info, String itf)
            throws IOException {
        while (!"java/lang/Object".equals(type)) {
            String[] itfs = info.getInterfaces();
            for (int i = 0; i < itfs.length; ++i) {
                if (itfs[i].equals(itf)) {
                    return true;
                }
            }
            for (int i = 0; i < itfs.length; ++i) {
                if (typeImplements(itfs[i], typeInfo(itfs[i]), itf)) {
                    return true;
                }
            }
            type = info.getSuperName();
            info = typeInfo(type);
        }
        return false;
    }
    
    /**
     * verilen sınıf veya interface'in ClassReader'ını döner
     *
     * @param type
     *            sınıf veya interface'in dahili adı.
     * @return  'type' dan sorumlu ClassReader.
     * @throws IOException
     *             'type' byte kodu yüklenemezse.
     * @throws NullPointerException
     *             'type' byte kodu bulunamazsa.
     */
    private ClassReader typeInfo(final String type) throws IOException, NullPointerException {
        InputStream is = l.getResourceAsStream(type + ".class");
        try {
        	if(is == null)
        		throw new NullPointerException("Class not found "+type);
            return new ClassReader(is);
        } finally {
        	if(is != null)
        		is.close();
        }
    }
}
