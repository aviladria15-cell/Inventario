package Modelo;

import java.sql.*;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class ConexiónBD {

    protected Connection con;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_GENERAL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String URL_INVENTARIO = "jdbc:mysql://localhost:3306/proyecto?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";

    public void conectar() throws ClassNotFoundException {
        try {
            Class.forName(DRIVER);

            boolean baseRecienCreada = false;

            // Verificar si la base de datos existe
            try (Connection tempCon = DriverManager.getConnection(URL_GENERAL, USUARIO, CONTRASEÑA);
                 Statement st = tempCon.createStatement()) {

                ResultSet rs = st.executeQuery("SHOW DATABASES LIKE 'proyecto'");
                if (!rs.next()) {
                    st.executeUpdate("CREATE DATABASE proyecto CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
                    baseRecienCreada = true;
                }
            }

            con = DriverManager.getConnection(URL_INVENTARIO, USUARIO, CONTRASEÑA);
            crearTablas();

            if (baseRecienCreada) {
                crearTriggers();
                crearDatosIniciales();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error al conectar con la base de datos:\n" +
                "Asegúrese de que MySQL esté ejecutándose.\n\n" +
                "Detalles: " + e.getMessage(),
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cerrarCn() throws SQLException {
        if (con != null && !con.isClosed()) con.close();
    }

    private void crearTablas() {
        String sql = """
            SET FOREIGN_KEY_CHECKS=0;

            CREATE TABLE IF NOT EXISTS `almacen` (
              `id_ubicacion` int(11) NOT NULL AUTO_INCREMENT,
              `pasillo` varchar(50) NOT NULL,
              `ala` enum('IZQUIERDA','DERECHA') NOT NULL,
              `estante` int(11) NOT NULL,
              `nivel` int(11) NOT NULL,
              `capacidad` int(11) DEFAULT NULL,
              `activa` enum('SI','NO') DEFAULT 'SI',
              PRIMARY KEY (`id_ubicacion`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `asiento_contable` (
              `id_asiento` int(11) NOT NULL AUTO_INCREMENT,
              `fecha` date DEFAULT curdate(),
              `descripcion` varchar(255) DEFAULT NULL,
              `referencia` varchar(50) DEFAULT NULL,
              `total_debe` decimal(18,2) DEFAULT 0.00,
              `total_haber` decimal(18,2) DEFAULT 0.00,
              PRIMARY KEY (`id_asiento`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `categoria` (
              `idCategoria` int(11) NOT NULL AUTO_INCREMENT,
              `nombre` varchar(50) DEFAULT NULL,
              PRIMARY KEY (`idCategoria`),
              UNIQUE KEY `nombre` (`nombre`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `empleado` (
              `idEmpleado` int(11) NOT NULL AUTO_INCREMENT,
              `nombre` varchar(50) DEFAULT NULL,
              `apellido` varchar(50) DEFAULT NULL,
              `cedula` varchar(15) DEFAULT NULL,
              `fecha_nacimiento` date DEFAULT NULL,
              `email` varchar(100) DEFAULT NULL,
              `telefono` varchar(20) DEFAULT NULL,
              `cargo` varchar(50) DEFAULT NULL,
              PRIMARY KEY (`idEmpleado`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `marca` (
              `idMarca` int(11) NOT NULL AUTO_INCREMENT,
              `nombre` varchar(50) DEFAULT NULL,
              `paisOrigen` varchar(50) DEFAULT NULL,
              PRIMARY KEY (`idMarca`),
              UNIQUE KEY `nombre` (`nombre`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `cuenta` (
              `id_cuenta` int(11) NOT NULL AUTO_INCREMENT,
              `codigo` varchar(25) NOT NULL,
              `nombre` varchar(255) NOT NULL,
              `tipo` varchar(45) NOT NULL,
              `descripcion` varchar(255) DEFAULT NULL,
              `saldo_inicial` decimal(15,2) NOT NULL DEFAULT 0.00,
              `nivel` int(11) NOT NULL DEFAULT 1,
              `parent_id` int(11) DEFAULT NULL,
              `activo` boolean DEFAULT TRUE,
              PRIMARY KEY (`id_cuenta`),
              UNIQUE KEY `codigo_unico` (`codigo`),
              CONSTRAINT `fk_cuenta_parent` FOREIGN KEY (`parent_id`) REFERENCES `cuenta` (`id_cuenta`) ON DELETE RESTRICT
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `proveedor` (
              `idProveedor` int(11) NOT NULL AUTO_INCREMENT,
              `nombre` varchar(100) DEFAULT NULL,
              `telefono` varchar(20) DEFAULT NULL,
              `direccion` varchar(100) DEFAULT NULL,
              `email` varchar(100) DEFAULT NULL,
              `RFC` varchar(20) DEFAULT NULL,
              PRIMARY KEY (`idProveedor`),
              UNIQUE KEY `telefono` (`telefono`),
              UNIQUE KEY `email` (`email`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `libro_diario` (
              `id_libro_diario` int(11) NOT NULL AUTO_INCREMENT,
              `id_asiento` int(11) NOT NULL,
              `id_cuenta` int(11) NOT NULL,
              `debe` decimal(18,2) DEFAULT 0.00,
              `haber` decimal(18,2) DEFAULT 0.00,
              PRIMARY KEY (`id_libro_diario`),
              KEY `id_asiento` (`id_asiento`),
              KEY `id_cuenta` (`id_cuenta`),
              CONSTRAINT `libro_diario_ibfk_1` FOREIGN KEY (`id_asiento`) REFERENCES `asiento_contable` (`id_asiento`) ON DELETE CASCADE,
              CONSTRAINT `libro_diario_ibfk_2` FOREIGN KEY (`id_cuenta`) REFERENCES `cuenta` (`id_cuenta`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `libro_mayor` (
              `id_libro_mayor` int(11) NOT NULL AUTO_INCREMENT,
              `id_cuenta` int(11) NOT NULL,
              `fecha` date NOT NULL,
              `saldo_anterior` decimal(18,2) DEFAULT 0.00,
              `debe` decimal(18,2) DEFAULT 0.00,
              `haber` decimal(18,2) DEFAULT 0.00,
              `saldo_final` decimal(18,2) DEFAULT 0.00,
              PRIMARY KEY (`id_libro_mayor`),
              KEY `id_cuenta` (`id_cuenta`),
              CONSTRAINT `libro_mayor_ibfk_1` FOREIGN KEY (`id_cuenta`) REFERENCES `cuenta` (`id_cuenta`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `producto` (
              `idProducto` int(11) NOT NULL AUTO_INCREMENT,
              `nombre` varchar(100) DEFAULT NULL,
              `tipo_Liquido` varchar(45) DEFAULT NULL,
              `viscosidad` varchar(45) DEFAULT NULL,
              `presentacion` varchar(45) DEFAULT NULL,
              `condicion` varchar(45) DEFAULT NULL,
              `compatibilidad` varchar(200) DEFAULT NULL,
              `numero_serial` varchar(45) DEFAULT NULL,
              `unidad_De_Medidad` varchar(45) DEFAULT NULL,
              `especificaciones` varchar(150) DEFAULT NULL,
              `densidad` varchar(45) DEFAULT NULL,
              `idCategoria` int(11) NOT NULL,
              `idMarca` int(11) NOT NULL,
              `tipo_producto` enum('SOLIDO','LIQUIDO','UNIDAD') DEFAULT NULL,
              PRIMARY KEY (`idProducto`),
              KEY `idCategoria` (`idCategoria`),
              KEY `idMarca` (`idMarca`),
              CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`),
              CONSTRAINT `producto_ibfk_2` FOREIGN KEY (`idMarca`) REFERENCES `marca` (`idMarca`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `usuario` (
              `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
              `nombreUsuario` varchar(50) DEFAULT NULL,
              `clave` varchar(255) DEFAULT NULL,
              `nivel_acceso` varchar(20) DEFAULT NULL,
              `estado` varchar(45) NOT NULL,
              `idEmpleado` int(11) DEFAULT NULL,
              PRIMARY KEY (`idUsuario`),
              KEY `idEmpleado` (`idEmpleado`),
              CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`idEmpleado`) REFERENCES `empleado` (`idEmpleado`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `inventario` (
              `id_inventario` int(11) NOT NULL AUTO_INCREMENT,
              `idProducto` int(11) DEFAULT NULL,
              `stockMinimo` int(11) DEFAULT NULL,
              `Cantidad` int(11) DEFAULT NULL,
              `precio_unitario` decimal(18,2) NOT NULL,
              `stockMaximo` int(11) DEFAULT NULL,
              `lote` varchar(45) DEFAULT NULL,
              `fecha_ingreso` timestamp NOT NULL DEFAULT current_timestamp(),
              `Fecha_Vencimiento` date DEFAULT NULL,
              `id_Ubicacion` int(11) DEFAULT NULL,
              `costo_total` decimal(18,2) DEFAULT NULL,
              `Cantidad_Disponible` int(11) NOT NULL DEFAULT 0,
              `tipo_producto` enum('LIQUIDO','SOLIDO','UNIDAD') NOT NULL,
              `Usuario` varchar(45) DEFAULT NULL,
              `precio_venta` decimal(18,2) DEFAULT NULL,
              `porcentaje` int(11) DEFAULT NULL,
              `idProveedor` int(11) NOT NULL,
              `numero_Factura` varchar(45) NOT NULL,
              `iva` int(11) DEFAULT NULL,
              PRIMARY KEY (`id_inventario`),
              KEY `idProducto` (`idProducto`),
              KEY `id_Ubicacion` (`id_Ubicacion`),
              KEY `idProveedor` (`idProveedor`),
              CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`) ON UPDATE CASCADE,
              CONSTRAINT `inventario_ibfk_2` FOREIGN KEY (`id_Ubicacion`) REFERENCES `almacen` (`id_ubicacion`),
              CONSTRAINT `inventario_ibfk_5` FOREIGN KEY (`idProveedor`) REFERENCES `proveedor` (`idProveedor`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `compra` (
              `id_compras` int(11) NOT NULL AUTO_INCREMENT,
              `id_inventario` int(11) NOT NULL,
              `numero_factura` varchar(45) NOT NULL,
              `idProveedor` int(11) NOT NULL,
              `idProducto` int(11) NOT NULL,
              `total_pagar` decimal(18,2) NOT NULL,
              `abono` decimal(18,2) DEFAULT NULL,
              `debe` decimal(18,2) DEFAULT NULL,
              `estado` varchar(45) NOT NULL,
              `id_cuenta` int(11) NOT NULL,
              `id_cuenta_destino` int(11) DEFAULT NULL,
              `forma_pago` varchar(45) NOT NULL,
              `fecha_pago` date DEFAULT NULL,
              PRIMARY KEY (`id_compras`),
              KEY `idx_id_inventario` (`id_inventario`),
              KEY `idx_id_idProveedor` (`idProveedor`),
              KEY `idx_idProducto` (`idProducto`),
              KEY `idx_id_cuenta` (`id_cuenta`),
              KEY `fk_compra_cuenta_destino` (`id_cuenta_destino`),
              CONSTRAINT `fk_compra_cuenta` FOREIGN KEY (`id_cuenta`) REFERENCES `cuenta` (`id_cuenta`) ON UPDATE CASCADE,
              CONSTRAINT `fk_compra_cuenta_destino` FOREIGN KEY (`id_cuenta_destino`) REFERENCES `cuenta` (`id_cuenta`),
              CONSTRAINT `fk_inventario_Proveedor` FOREIGN KEY (`idProveedor`) REFERENCES `inventario` (`idProveedor`) ON UPDATE CASCADE,
              CONSTRAINT `fk_inventario_compras` FOREIGN KEY (`id_inventario`) REFERENCES `inventario` (`id_inventario`) ON UPDATE CASCADE,
              CONSTRAINT `fk_inventario_producto` FOREIGN KEY (`idProducto`) REFERENCES `inventario` (`idProducto`) ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            CREATE TABLE IF NOT EXISTS `movimiento` (
              `ID` int(11) NOT NULL AUTO_INCREMENT,
              `idProducto` int(11) NOT NULL,
              `id_inventario` int(11) DEFAULT NULL,
              `Tipo_Producto` varchar(100) NOT NULL,
              `Movimiento` enum('ENTRADA','AJUSTE','SALIDA') NOT NULL,
              `Cantidad` int(11) NOT NULL,
              `StockFinal` int(11) DEFAULT 0,
              `Usuario` varchar(50) NOT NULL,
              `Detalle` text DEFAULT NULL,
              `FechaMovimiento` datetime DEFAULT current_timestamp(),
              PRIMARY KEY (`ID`),
              KEY `idProducto` (`idProducto`),
              KEY `id_inventario` (`id_inventario`),
              CONSTRAINT `movimiento_ibfk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`) ON UPDATE CASCADE,
              CONSTRAINT `movimiento_ibfk_2` FOREIGN KEY (`id_inventario`) REFERENCES `inventario` (`id_inventario`) ON DELETE CASCADE ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

            SET FOREIGN_KEY_CHECKS=1;
            """;

        try (Statement st = con.createStatement()) {
            String[] sentencias = sql.split(";");
            for (String sentencia : sentencias) {
                sentencia = sentencia.trim();
                if (!sentencia.isEmpty()) {
                    st.execute(sentencia);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear tablas:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearTriggers() {
        try (Statement st = con.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS=0;");

            String[] triggerList = {"trg_bloquear_eliminar_categoria", "trg_bloquear_eliminar_marca", "set_tipo_producto"};
            for (String trig : triggerList) {
                try { st.execute("DROP TRIGGER IF EXISTS " + trig); } catch (Exception ignored) {}
            }

            st.execute("""
                CREATE TRIGGER IF NOT EXISTS trg_bloquear_eliminar_categoria 
                BEFORE DELETE ON categoria FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM producto WHERE idCategoria = OLD.idCategoria) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar la categoría: hay productos asignados.';
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER IF NOT EXISTS trg_bloquear_eliminar_marca 
                BEFORE DELETE ON marca FOR EACH ROW
                BEGIN
                    IF EXISTS (SELECT 1 FROM producto WHERE idMarca = OLD.idMarca) THEN
                        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar la marca: hay productos asignados.';
                    END IF;
                END
                """);

            st.execute("""
                CREATE TRIGGER IF NOT EXISTS set_tipo_producto 
                BEFORE INSERT ON producto FOR EACH ROW
                BEGIN
                    IF NEW.tipo_Liquido IS NOT NULL AND NEW.tipo_Liquido <> '' THEN
                        SET NEW.tipo_producto = 'LIQUIDO';
                    ELSEIF NEW.condicion IS NOT NULL AND NEW.condicion <> '' THEN
                        SET NEW.tipo_producto = 'SOLIDO';
                    ELSEIF (NEW.especificaciones IS NOT NULL AND NEW.especificaciones <> '')
                       AND (NEW.numero_serial IS NOT NULL AND NEW.numero_serial <> '') THEN
                        SET NEW.tipo_producto = 'UNIDAD';
                    END IF;
                END
                """);

            st.execute("SET FOREIGN_KEY_CHECKS=1;");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear triggers:\n" + e.getMessage());
        }
    }

    private void crearDatosIniciales() {
        try {
            // 1. INSERTAR EMPLEADO ADMINISTRADOR
            String sql = "INSERT IGNORE INTO empleado (nombre, apellido, cedula, fecha_nacimiento, email, telefono, cargo) " +
                         "VALUES ('Admin', 'Sistema', '0000000000', '2000-01-01', 'bot@sistema.com', '0000000000', 'Dueño')";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            int idEmpleado = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) idEmpleado = rs.getInt(1);
            rs.close();
            ps.close();

            // 2. INSERTAR USUARIO ADMINISTRADOR
            String contrasenaPlana = "inventario.01";
            String contrasenaCifrada = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(12));
            
            sql = "INSERT IGNORE INTO usuario (nombreUsuario, clave, nivel_acceso, estado, idEmpleado) " +
                  "VALUES ('Admin', ?, 'Alto', 'Activo', ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, contrasenaCifrada);
            ps.setInt(2, idEmpleado);
            ps.executeUpdate();
            ps.close();

            // 3. POBLAR TABLA CUENTA CON EL PCP 2024 (INCLUYENDO COLUMNA ACTIVO)
            Statement stmt = con.createStatement();
            
            // NIVEL 1: GRUPOS
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(1, '1', 'ACTIVO', 'Deudora', 'Representa los bienes y derechos del ente', 0.00, 1, NULL, true), " +
                         "(2, '2', 'PASIVO', 'Acreedora', 'Representa las obligaciones y deudas del ente', 0.00, 1, NULL, true), " +
                         "(3, '3', 'PATRIMONIO', 'Acreedora', 'Representa el patrimonio neto acumulado', 0.00, 1, NULL, true), " +
                         "(4, '4', 'INGRESOS', 'Acreedora', 'Ingresos públicos ordinarios y extraordinarios', 0.00, 1, NULL, true), " +
                         "(5, '5', 'GASTOS', 'Deudora', 'Gastos operativos, de personal y transferencias', 0.00, 1, NULL, true), " +
                         "(6, '6', 'CUENTAS DE ORDEN', 'Deudora', 'Cuentas de control y contingencias', 0.00, 1, NULL, true), " +
                         "(7, '7', 'CUENTAS DE CIERRE', 'Acreedora', 'Cuentas utilizadas para el cierre del ejercicio', 0.00, 1, NULL, true)");

            // NIVEL 2: SUBGRUPOS
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(8, '1.1', 'ACTIVO CORRIENTE', 'Deudora', 'Bienes y derechos líquidos o realizables a corto plazo', 0.00, 2, 1, true), " +
                         "(9, '1.2', 'ACTIVO NO CORRIENTE', 'Deudora', 'Bienes y derechos a largo plazo, propiedades y equipos', 0.00, 2, 1, true), " +
                         "(10, '2.1', 'PASIVO CORRIENTE', 'Acreedora', 'Obligaciones exigibles a corto plazo', 0.00, 2, 2, true), " +
                         "(11, '2.2', 'PASIVO NO CORRIENTE', 'Acreedora', 'Obligaciones y deudas a largo plazo', 0.00, 2, 2, true), " +
                         "(12, '3.1', 'PATRIMONIO NETO', 'Acreedora', 'Capital, reservas y resultados acumulados', 0.00, 2, 3, true), " +
                         "(13, '4.1', 'INGRESOS ORDINARIOS', 'Acreedora', 'Ingresos recurrentes por tributos o gestión', 0.00, 2, 4, true), " +
                         "(14, '4.2', 'INGRESOS EXTRAORDINARIOS', 'Acreedora', 'Ingresos no recurrentes del ente público', 0.00, 2, 4, true), " +
                         "(15, '5.1', 'GASTOS DE PERSONAL', 'Deudora', 'Sueldos, salarios y beneficios del personal', 0.00, 2, 5, true), " +
                         "(16, '5.2', 'GASTOS OPERATIVOS', 'Deudora', 'Compra de bienes y servicios para el funcionamiento', 0.00, 2, 5, true), " +
                         "(17, '6.1', 'CUENTAS DE ORDEN DEUDORAS', 'Deudora', 'Responsabilidades e intereses deudores', 0.00, 2, 6, true), " +
                         "(18, '6.2', 'CUENTAS DE ORDEN ACREEDORAS', 'Acreedora', 'Responsabilidades e intereses acreedores', 0.00, 2, 6, true), " +
                         "(19, '7.1', 'CIERRE DEL EJERCICIO ECONOMICO FINANCIERO', 'Acreedora', 'Operaciones finales de cuadre de saldos', 0.00, 2, 7, true)");

            // NIVEL 3: RUBROS
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(21, '1.1.1', 'DISPONIBILIDADES', 'Deudora', 'Efectivo en caja y cuentas bancarias del ente', 0.00, 3, 8, true), " +
                         "(22, '1.1.2', 'INVERSIONES FINANCIERAS', 'Deudora', 'Colocaciones financieras a corto plazo', 0.00, 3, 8, true), " +
                         "(23, '1.1.3', 'CUENTAS POR COBRAR', 'Deudora', 'Derechos de cobro exigibles en el corto plazo', 0.00, 3, 8, true), " +
                         "(24, '1.2.1', 'PROPIEDADES, PLANTA Y EQUIPOS', 'Deudora', 'Bienes tangibles e inmuebles de uso institucional', 0.00, 3, 9, true), " +
                         "(25, '1.2.2', 'BIENES DE USO PUBLICO','Deudora', 'Bienes del dominio público', 0.00, 3, 9, true), " +
                         "(26, '2.1.1', 'CUENTAS POR PAGAR A CORTO PLAZO', 'Acreedora', 'Obligaciones comerciales e institucionales inmediatas', 0.00, 3, 10, true), " +
                         "(27, '3.1.1', 'CAPITAL', 'Acreedora', 'Aportes iniciales e institucionales', 0.00, 3, 12, true), " +
                         "(28, '3.1.2', 'RESULTADOS ACUMULADOS', 'Acreedora', 'Excedentes o déficits de ejercicios anteriores', 0.00, 3, 12, true), " +
                         "(29, '4.1.1', 'INGRESOS TRIBUTARIOS', 'Acreedora', 'Recaudación de impuestos y tasas normadas', 0.00, 3, 13, true), " +
                         "(30, '5.1.1', 'REMUNERACIONES', 'Deudora', 'Pagos fijos y asignaciones básicas de ley', 0.00, 3, 15, true), " +
                         "(31, '7.1.1', 'RESUMEN DE INGRESOS Y GASTOS', 'Acreedora', 'Agrupación de saldos del periodo analizado', 0.00, 3, 19, true), " +
                         "(32, '7.1.2', 'RESULTADO DE LA GESTIÓN', 'Acreedora', 'Determinación del ahorro o desahorro final', 0.00, 3, 19, true)");

            // NIVEL 4: CUENTAS
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(36, '1.1.1.01', 'EFECTIVO EN CAJA', 'Deudora', 'Fondos en posesión directa de las cajas del ente', 0.00, 4, 21, true), " +
                         "(37, '1.1.1.02', 'BANCOS', 'Deudora', 'Efectivo depositado en instituciones bancarias', 0.00, 4, 21, true), " +
                         "(38, '1.1.2.01', 'COLOCACIONES A PLAZO', 'Deudora', 'Depósitos e instrumentos de inversión temporal', 0.00, 4, 22, true), " +
                         "(39, '1.1.3.01', 'IMPUESTOS POR COBRAR', 'Deudora', 'Derechos tributarios liquidados pendientes de percibir', 0.00, 4, 23, true), " +
                         "(40, '1.2.1.01', 'EDIFICACIONES', 'Deudora', 'Estructuras edilicias propiedad del ente', 0.00, 4, 24, true), " +
                         "(41, '1.2.1.02', 'EQUIPOS DE COMPUTACIÓN', 'Deudora', 'Hardware, servidores y periféricos operativos', 0.00, 4, 24, true), " +
                         "(42, '7.1.1.01', 'RESUMEN DE INGRESOS Y GASTOS', 'Acreedora', 'Cuenta liquidadora de saldos del año fiscal', 0.00, 4, 31, true)");

            // NIVEL 5: SUBCUENTAS DE 1ER ORDEN
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(51, '1.1.1.01.01', 'CAJA CHICA', 'Deudora', 'Fondos fijos asignados a gastos menores inmediatos', 0.00, 5, 36, true), " +
                         "(52, '1.1.1.01.02', 'CAJA PRINCIPAL', 'Deudora', 'Fondos centrales de recaudación diaria', 0.00, 5, 36, true), " +
                         "(53, '1.1.1.02.01', 'BANCO CENTRAL DE VENEZUELA', 'Deudora', 'Cuentas operativas mantenidas en el BCV', 0.00, 5, 37, true), " +
                         "(54, '1.1.1.02.02', 'BANCOS NACIONALES', 'Deudora', 'Cuentas en instituciones de la banca pública o privada', 0.00, 5, 37, true), " +
                         "(55, '1.2.1.02.01', 'EQUIPOS DE PROCESAMIENTO DE DATOS', 'Deudora', 'Laptops, computadoras de escritorio y servidores', 0.00, 5, 41, true)");

            // NIVEL 6: SUBCUENTAS DE 2DO ORDEN
            stmt.addBatch("INSERT IGNORE INTO cuenta (id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES " +
                         "(66, '1.1.1.02.02.01', 'BANCO DE VENEZUELA S.A.', 'Deudora', 'Cuenta corriente única institucional BDV', 0.00, 6, 54, true), " +
                         "(67, '1.1.1.02.02.02', 'BANCO DEL TESORO', 'Deudora', 'Fondos asignados en la entidad Banco del Tesoro', 0.00, 6, 54, true), " +
                         "(68, '1.1.1.01.01.01', 'CAJA CHICA ADMINISTRACIÓN', 'Deudora', 'Fondo de uso exclusivo del departamento administrativo', 0.00, 6, 51, true), " +
                         "(69, '1.1.1.01.01.02', 'CAJA CHICA OPERACIONES', 'Deudora', 'Fondo operativo para compras e imprevistos de campo', 0.00, 6, 51, true)");

            stmt.executeBatch();
            stmt.close();

            JOptionPane.showMessageDialog(null,
                "✅ Base de datos 'proyecto' lista y unificada.\n" +
                "Se ha cargado la jerarquía del Plan de Cuentas Patrimoniales 2024.\n\n" +
                "Usuario: Admin\nContraseña: inventario.01",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean estaConectado() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return con;
    }
}