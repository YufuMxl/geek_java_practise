package first_week;

import sun.misc.Launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Base64;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) {
        // 类的动态加载，可以对代码进行加密保护
        String hello_base_64 =
            "yv66vgAAADQAHAoABgAOCQAPABAIABEKABIAEwcAFAcAFQEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBAAg8Y2xpbml0PgEAClNvdXJjZUZpbGUBAApIZWxsby5qYXZhDAAHAAgHABYMABcAGAEACeS9oOWlveWRgAcAGQwAGgAbAQAQZmlyc3Rfd2Vlay9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAFAAYAAAAAAAIAAQAHAAgAAQAJAAAAHQABAAEAAAAFKrcAAbEAAAABAAoAAAAGAAEAAAADAAgACwAIAAEACQAAACUAAgAAAAAACbIAAhIDtgAEsQAAAAEACgAAAAoAAgAAAAUACAAGAAEADAAAAAIADQ==";
        byte[] bytes = Base64.getDecoder().decode(hello_base_64);
        return defineClass(name, bytes, 0, bytes.length);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        // 查看启动类加载器的加载内容
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        System.out.println("启动类加载器");
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }

        // 查看扩展类加载器的加载内容
        URLClassLoader extClassLoader = (URLClassLoader) MyClassLoader.class.getClassLoader().getParent();
        System.out.println("扩展类加载器");
        for (URL url : extClassLoader.getURLs()) {
            System.out.println(url.toExternalForm());
        }

        // 查看应用类加载器的加载内容
        URLClassLoader appClassLoader = (URLClassLoader) MyClassLoader.class.getClassLoader();
        System.out.println("应用类加载器");
        for (URL url : appClassLoader.getURLs()) {
            System.out.println(url.toExternalForm());
        }

        new MyClassLoader().findClass("first_week.Hello").newInstance();
    }

}
