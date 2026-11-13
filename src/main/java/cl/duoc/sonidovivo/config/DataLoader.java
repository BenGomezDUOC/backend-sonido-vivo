package cl.duoc.sonidovivo.config;

import cl.duoc.sonidovivo.model.Producto;
import cl.duoc.sonidovivo.model.Rol;
import cl.duoc.sonidovivo.model.Usuario;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

/**
 * Pre-carga automática del catálogo oficial de Tienda Sonido Vivo y usuarios iniciales en MySQL.
 */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Pre-carga de Usuarios (Admin y Cliente Duoc)
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario(
                        null,
                        "Administrador Sonido Vivo",
                        "admin@sonidovivo.cl",
                        passwordEncoder.encode("duoc123"),
                        Rol.ROLE_ADMIN,
                        LocalDate.of(1995, 5, 20),
                        false
                );

                Usuario clienteDuoc = new Usuario(
                        null,
                        "Benjamín Gómez",
                        "alumno@duocuc.cl",
                        passwordEncoder.encode("duoc123"),
                        Rol.ROLE_CLIENTE,
                        LocalDate.of(2002, 10, 15),
                        true
                );

                usuarioRepository.saveAll(List.of(admin, clienteDuoc));
                System.out.println(">>> [Tienda Sonido Vivo] Usuarios iniciales (Admin y Cliente Duoc) creados en MySQL.");
            }

            // 2. Pre-carga de los 12 Equipos de Audio Oficiales
            if (productoRepository.count() == 0) {
                List<Producto> productosIniciales = List.of(
                        new Producto(null, "Audífonos de Estudio Audio-Technica ATH-M50x", 159990, "Audífonos",
                                "/img/Audífonos de Estudio Audio-Technica ATH-M50x.png",
                                "Audífonos profesionales de monitorización aclamados por ingenieros de sonido.", 15),

                        new Producto(null, "Micrófono Dinámico Vocal Shure SM7B", 429990, "Micrófonos",
                                "/img/Micrófono Dinámico Vocal Shure SM7B.webp",
                                "Micrófono legendario de radiodifusión y voces de estudio con blindaje electromagnético.", 8),

                        new Producto(null, "Interfaz de Audio Focusrite Scarlett 2i2 4th Gen", 219990, "Interfaces de Audio",
                                "/img/Interfaz de Audio Focusrite Scarlett 2i2 4th Gen.jpg",
                                "Interfaz USB-C de 2 entradas y 2 salidas con preamplificadores de ruido ultra bajo y modo Air.", 12),

                        new Producto(null, "Monitores de Estudio Yamaha HS5 (Par)", 389990, "Monitores y Parlantes",
                                "/img/Monitores de Estudio Yamaha HS5 (Par).jpg",
                                "Monitores biamplificados de campo cercano con conos blancos icónicos para mezclas precisas.", 6),

                        new Producto(null, "Parlante Bluetooth Marshall Acton III", 279990, "Monitores y Parlantes",
                                "/img/Parlante Bluetooth Marshall Acton III.webp",
                                "Diseño clásico vintage con sonido estéreo envolvente y conectividad Bluetooth 5.2.", 10),

                        new Producto(null, "Controlador MIDI Novation Launchkey 49 MK3", 249990, "Instrumentos y Mezcladores",
                                "/img/Controlador MIDI Novation Launchkey 49 MK3.png",
                                "Teclado controlador de 49 teclas sensibles con pads RGB e integración total con DAWs.", 7),

                        new Producto(null, "Micrófono USB Rode NT-USB Mini", 119990, "Micrófonos",
                                "/img/Micrófono USB Rode NT-USB Mini.jpg",
                                "Calidad de audio de estudio en un formato compacto USB para podcasting y streaming.", 14),

                        new Producto(null, "Audífonos Inalámbricos Sony WH-1000XM5 con Noise Cancelling", 359990, "Audífonos",
                                "/img/Audífonos Inalámbricos Sony WH-1000XM5.webp",
                                "Cancelación de ruido líder en la industria con procesador V1 y soporte para audio Hi-Res LDAC.", 9),

                        new Producto(null, "Consola de Mezcla Behringer Xenyx Q802USB", 109990, "Instrumentos y Mezcladores",
                                "/img/Consola de Mezcla Behringer Xenyx Q802USB.jpg",
                                "Mezclador análogo premium de 8 entradas con 2 preamplificadores Xenyx y compresores.", 11),

                        new Producto(null, "Audífonos In-Ear Profesionales Shure SE215 PRO", 119990, "Audífonos",
                                "/img/Audífonos In-Ear Profesionales Shure SE215 PRO.avif",
                                "Monitores intraurales con microtransductor dinámico y aislamiento de sonido de hasta 37 dB.", 20),

                        new Producto(null, "Tornamesa de Tracción Directa Audio-Technica AT-LP120XUSB", 399990, "Instrumentos y Mezcladores",
                                "/img/Tornamesa Audio-Technica AT-LP120XUSB.jpg",
                                "Tocadiscos profesional para DJ y audiófilos con motor de tracción directa y salida USB.", 5),

                        new Producto(null, "Pack Cables de Micrófono XLR Macho a Hembra 5m (Par)", 24990, "Accesorios",
                                "/img/Pack Cables de Micrófono XLR Macho a Hembra 5m.webp",
                                "Conductores de cobre libre de oxígeno con conectores metálicos blindados para bajo ruido.", 30)
                );

                productoRepository.saveAll(productosIniciales);
                System.out.println(">>> [Tienda Sonido Vivo] Catálogo oficial de 12 productos cargado exitosamente en MySQL.");
            }
        };
    }
}
