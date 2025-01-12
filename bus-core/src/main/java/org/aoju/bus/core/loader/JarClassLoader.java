/*
 * The MIT License
 *
 * Copyright (c) 2017, aoju.org All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aoju.bus.core.loader;

import org.aoju.bus.core.lang.exception.CommonException;
import org.aoju.bus.core.utils.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 外部Jar的类加载器
 *
 * @author Kimi Liu
 * @version 3.0.5
 * @since JDK 1.8
 */
public class JarClassLoader extends URLClassLoader {

    /**
     * 构造
     */
    public JarClassLoader() {
        this(new URL[]{});
    }

    /**
     * 构造
     *
     * @param urls 被加载的URL
     */
    public JarClassLoader(URL[] urls) {
        super(urls, ClassUtils.getClassLoader());
    }

    /**
     * 加载Jar到ClassPath
     *
     * @param dir jar文件或所在目录
     * @return JarClassLoader
     */
    public static JarClassLoader load(File dir) {
        final JarClassLoader loader = new JarClassLoader();
        //查找加载所有jar
        loader.addJar(dir);
        //查找加载所有class
        loader.addURL(dir);
        return loader;
    }

    /**
     * 加载Jar到ClassPath
     *
     * @param jarFile jar文件或所在目录
     * @return JarClassLoader
     */
    public static JarClassLoader loadJar(File jarFile) {
        final JarClassLoader loader = new JarClassLoader();
        try {
            loader.addJar(jarFile);
        } finally {
            IoUtils.close(loader);
        }
        return loader;
    }

    /**
     * 加载Jar文件到指定loader中
     *
     * @param loader  {@link URLClassLoader}
     * @param jarFile 被加载的jar
     * @throws CommonException IO异常包装和执行异常
     */
    public static void loadJar(URLClassLoader loader, File jarFile) throws CommonException {
        try {
            final Method method = ClassUtils.getDeclaredMethod(URLClassLoader.class, "addURL", URL.class);
            if (null != method) {
                method.setAccessible(true);
                final List<File> jars = loopJar(jarFile);
                for (File jar : jars) {
                    ReflectUtils.invoke(loader, method, jar.toURI().toURL());
                }
            }
        } catch (IOException e) {
            throw new CommonException(e);
        }
    }

    /**
     * 加载Jar文件到System ClassLoader中
     *
     * @param jarFile 被加载的jar
     * @return System ClassLoader
     */
    public static URLClassLoader loadJarToSystemClassLoader(File jarFile) {
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        loadJar(urlClassLoader, jarFile);
        return urlClassLoader;
    }

    /**
     * 递归获得Jar文件
     *
     * @param file jar文件或者包含jar文件的目录
     * @return jar文件列表
     */
    private static List<File> loopJar(File file) {
        return FileUtils.loopFiles(file, new FileFilter() {

            @Override
            public boolean accept(File file) {
                final String path = file.getPath();
                return path != null && path.toLowerCase().endsWith(".jar");
            }
        });
    }

    /**
     * 是否为jar文件
     *
     * @param file 文件
     * @return 是否为jar文件
     * @since 4.4.2
     */
    private static boolean isJarFile(File file) {
        if (false == FileUtils.isFile(file)) {
            return false;
        }
        return file.getPath().toLowerCase().endsWith(".jar");
    }

    /**
     * 加载Jar文件，或者加载目录
     *
     * @param jarFile jar文件或者jar文件所在目录
     * @return this
     */
    public JarClassLoader addJar(File jarFile) {
        final List<File> jars = loopJar(jarFile);
        try {
            for (File jar : jars) {
                super.addURL(jar.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            throw new CommonException(e);
        }
        return this;
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    /**
     * 增加class所在目录或文件
     * 如果为目录，此目录用于搜索class文件，如果为文件，需为jar文件
     *
     * @param dir 目录
     * @return the jarClassLoader
     * @since 4.4.2
     */
    public JarClassLoader addURL(File dir) {
        super.addURL(URLUtils.getURL(dir));
        return this;
    }

}
