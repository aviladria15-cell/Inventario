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

            CREATE TABLE IF NOT EXISTS `cuenta` (
              `id_cuenta` int(11) NOT NULL AUTO_INCREMENT,
              `codigo` varchar(20) NOT NULL,
              `nombre` varchar(100) NOT NULL,
              `tipo` enum('ACTIVO','PASIVO','PATRIMONIO','INGRESO','GASTO') NOT NULL,
              `descripcion` varchar(255) DEFAULT NULL,
              `saldo_inicial` decimal(18,2) DEFAULT 0.00,
              PRIMARY KEY (`id_cuenta`),
              UNIQUE KEY `codigo` (`codigo`)
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
            """;

        try (Statement st = con.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS=0;");
            
            String[] sentencias = sql.split(";");
            for (String sentencia : sentencias) {
                sentencia = sentencia.trim();
                if (!sentencia.isEmpty()) {
                    st.execute(sentencia);
                }
            }
            
            st.execute("SET FOREIGN_KEY_CHECKS=1;");
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
            String sql = "INSERT IGNORE INTO empleado (nombre, apellido, cedula, fecha_nacimiento, email, telefono, cargo) " +
                         "VALUES ('Admin', 'Sistema', '0000000000', '2000-01-01', 'bot@sistema.com', '0000000000', 'Dueño')";
            
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            int idEmpleado = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) idEmpleado = rs.getInt(1);
            rs.close();
            ps.close();

            String contrasenaPlana = "inventario.01";
            String contrasenaCifrada = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(12));

            sql = "INSERT IGNORE INTO usuario (nombreUsuario, clave, nivel_acceso, estado, idEmpleado) " +
                  "VALUES ('Admin', ?, 'Alto', 'Activo', ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, contrasenaCifrada);
            ps.setInt(2, idEmpleado);
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null,
                "✅ Base de datos 'proyecto' lista.\n\n" +
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