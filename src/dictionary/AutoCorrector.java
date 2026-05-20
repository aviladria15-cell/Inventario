package dictionary;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AutoCorrector {
    private static final Map<String, String> diccionario = new HashMap<>();

    static {
      diccionario.put("qe", "que");
        diccionario.put("ola", "hola");
        diccionario.put("xq", "porque");
        diccionario.put("toyoota", "Toyota");
        diccionario.put("fordd", "Ford");
        diccionario.put("chebrolet", "Chevrolet");
        diccionario.put("hondaah", "Honda");
        diccionario.put("nisan", "Nissan");
        diccionario.put("volksvagen", "Volkswagen");
        diccionario.put("hiunday", "Hyundai");
        diccionario.put("kiaa", "Kia");
        diccionario.put("Sintetico" , "sintético");
        diccionario.put("bmwz", "BMW");
        diccionario.put("mercedezbenz", "MercedesBenz");
        diccionario.put("audiio", "Audi");
        diccionario.put("renol", "Renault");
        diccionario.put("peujot", "Peugeot");
        diccionario.put("citron", "Citroen");
        diccionario.put("fiatt", "Fiat");
        diccionario.put("opell", "Opel");
        diccionario.put("subarru", "Subaru");
        diccionario.put("mazdaa", "Mazda");
        diccionario.put("mitzubichi", "Mitsubishi");
        diccionario.put("teslla", "Tesla");
        diccionario.put("bolvo", "Volvo");
        diccionario.put("jaguarrr", "Jaguar");
        diccionario.put("landroverr", "LandRover");
        diccionario.put("porshe", "Porsche");
        diccionario.put("susuki", "Suzuki");
        diccionario.put("escoda", "Skoda");
        diccionario.put("seattt", "Seat");
        diccionario.put("infinity", "Infiniti");
        diccionario.put("lexsus", "Lexus");
        diccionario.put("minii", "Mini");
        diccionario.put("bossh", "Bosch");
        diccionario.put("densoo", "Denso");
        diccionario.put("valleo", "Valeo");
        diccionario.put("delffi", "Delphi");
        diccionario.put("brembbo", "Brembo");
        diccionario.put("trrww", "TRW");
        diccionario.put("zeeef", "ZF");
        diccionario.put("atee", "ATE");
        diccionario.put("enggk", "NGK");
        diccionario.put("mahleh", "Mahle");
        diccionario.put("man", "Mann");
        diccionario.put("gatees", "Gates");
        diccionario.put("eskf", "SKF");
        diccionario.put("timkem", "Timken");
        diccionario.put("monro", "Monroe");
        diccionario.put("kayb", "KYB");
        diccionario.put("sachss", "Sachs");
        diccionario.put("bilsten", "Bilstein");
        diccionario.put("jela", "Hella");
        diccionario.put("magnetimarely", "MagnetiMarelli");
        diccionario.put("lukas", "Lucas");
        diccionario.put("dayko", "Dayco");
        diccionario.put("acedelco", "ACDelco");
        diccionario.put("feebi", "Febi");
        diccionario.put("berr", "Behr");
        diccionario.put("erra", "ERA");
        diccionario.put("elrinq", "Elring");
        diccionario.put("powerstopp", "PowerStop");
        diccionario.put("ebbc", "EBC");
        diccionario.put("waker", "Walker");
        diccionario.put("frammm", "Fram");
        diccionario.put("remmi", "Remy");
        diccionario.put("yasaki", "Yazaki");
        diccionario.put("kontinental", "Continental");
        diccionario.put("michellin", "Michelin");
        diccionario.put("bridstong", "Bridgestone");
        diccionario.put("gudyear", "Goodyear");
        diccionario.put("pirely", "Pirelli");
        diccionario.put("aysin", "Aisin");
        diccionario.put("jitachi", "Hitachi");

        // 🔹 Autopartes
        diccionario.put("motorrr", "motor");
        diccionario.put("culatta", "culata");
        diccionario.put("cigueñal", "ciguenal");
        diccionario.put("valbula", "valvula");
        diccionario.put("coletor", "colector");
        diccionario.put("silensiador", "silenciador");
        diccionario.put("catalisador", "catalizador");
        diccionario.put("turboo", "turbo");
        diccionario.put("interculer", "intercooler");
        diccionario.put("filtroo", "filtro");
        diccionario.put("bujjia", "bujia");
        diccionario.put("bobin", "bobina");
        diccionario.put("distrivuidor", "distribuidor");
        diccionario.put("alternadorr", "alternador");
        diccionario.put("radiadoor", "radiador");
        diccionario.put("termostatoo", "termostato");
        diccionario.put("ventilador", "ventilador");
        diccionario.put("deposito", "deposito");
        diccionario.put("manguera", "manguera");
        diccionario.put("bomba", "bomba");
        diccionario.put("condensador", "condensador");
        diccionario.put("evaporador", "evaporador");
        diccionario.put("compresor", "compresor");
        diccionario.put("embrague", "embrague");
        diccionario.put("volante", "volante");
        diccionario.put("palier", "palier");
        diccionario.put("diferencial", "diferencial");
        diccionario.put("soporte", "soporte");
        diccionario.put("disco", "disco");
        diccionario.put("pastilla", "pastilla");
        diccionario.put("pinza", "pinza");
        diccionario.put("tambor", "tambor");
        diccionario.put("zapatas", "zapatas");
        diccionario.put("sensor", "sensor");
        diccionario.put("amortiguador", "amortiguador");
        diccionario.put("resorte", "resorte");
        diccionario.put("rotula", "rotula");
        diccionario.put("buje", "buje");
        diccionario.put("cremallera", "cremallera");
        diccionario.put("direccion", "direccion");
        diccionario.put("bateria", "bateria");
        diccionario.put("fusible", "fusible");
        diccionario.put("rele", "rele");
        diccionario.put("cableado", "cableado");
        diccionario.put("ecu", "ecu");
        diccionario.put("modulo", "modulo");
        diccionario.put("faros", "faros");
        diccionario.put("lampara", "lampara");
        diccionario.put("tablero", "tablero");
        diccionario.put("camara", "camara");
        diccionario.put("parachoques", "parachoques");
        diccionario.put("capo", "capo");
        diccionario.put("maletero", "maletero");
        diccionario.put("puerta", "puerta");
        diccionario.put("espejo", "espejo");
        diccionario.put("cristal", "cristal");
        diccionario.put("parabrisas", "parabrisas");
        diccionario.put("guardabarros", "guardabarros");
        diccionario.put("asiento", "asiento");
        diccionario.put("cinturon", "cinturon");
        diccionario.put("airbag", "airbag");
        diccionario.put("llanta", "llanta");
        diccionario.put("neumatico", "neumatico");
        diccionario.put("tornillo", "tornillo");
        diccionario.put("aceite", "aceite");
        diccionario.put("refrigerante", "refrigerante");
        diccionario.put("anticongelante", "anticongelante");
        diccionario.put("escobilla", "escobilla");
        diccionario.put("adblue", "adblue");
        diccionario.put("junta", "junta");
        diccionario.put("reten", "reten");
        diccionario.put("rodamiento", "rodamiento");
        diccionario.put("tornilleria", "tornilleria");

        // 🔹 Nombres masculinos
        diccionario.put("juann", "Juan");
        diccionario.put("josee", "José");
        diccionario.put("carlosss", "Carlos");
        diccionario.put("andress", "Andrés");
        diccionario.put("migel", "Miguel");
        diccionario.put("luisito", "Luis");
        diccionario.put("pedroo", "Pedro");
        diccionario.put("antonioo", "Antonio");
        diccionario.put("daviddd", "David");
        diccionario.put("fransisco", "Francisco");
        diccionario.put("alejandroo", "Alejandro");
        diccionario.put("manuell", "Manuel");
        diccionario.put("jorj", "Jorge");
        diccionario.put("ricarrdo", "Ricardo");
        diccionario.put("fernanddo", "Fernando");
        diccionario.put("roberrt", "Roberto");

        // 🔹 Nombres femeninos
        diccionario.put("mariaa", "María");
        diccionario.put("carmen", "Carmen");
        diccionario.put("luisaa", "Luisa");
        diccionario.put("anaaa", "Ana");
        diccionario.put("sofiia", "Sofía");
        diccionario.put("isabell", "Isabel");
        diccionario.put("rosaa", "Rosa");
        diccionario.put("paty", "Patricia");
        diccionario.put("lauraa", "Laura");
        diccionario.put("gabyy", "Gabriela");
        diccionario.put("paolaa", "Paola");
        diccionario.put("valentinaa", "Valentina");
        diccionario.put("danielaa", "Daniela");
        diccionario.put("claudiia", "Claudia");
        diccionario.put("andreaa", "Andrea");
        diccionario.put("monicaa", "Mónica");
        
        // 🔹 MARCAS DE VEHÍCULOS (50+ adicionales)
diccionario.put("alfaromeoo", "Alfa Romeo");
diccionario.put("astonmartinn", "Aston Martin");
diccionario.put("bentleyy", "Bentley");
diccionario.put("bugattii", "Bugatti");
diccionario.put("cadillacc", "Cadillac");
diccionario.put("chryslerr", "Chrysler");
diccionario.put("dodgeee", "Dodge");
diccionario.put("ferrarii", "Ferrari");
diccionario.put("fiskerr", "Fisker");
diccionario.put("genesiss", "Genesis");
diccionario.put("hummerr", "Hummer");
diccionario.put("lamborghinii", "Lamborghini");
diccionario.put("lanciaa", "Lancia");
diccionario.put("lotuss", "Lotus");
diccionario.put("maserattii", "Maserati");
diccionario.put("maybachh", "Maybach");
diccionario.put("mclarenn", "McLaren");
diccionario.put("paganii", "Pagani");
diccionario.put("polestarr", "Polestar");
diccionario.put("riviann", "Rivian");
diccionario.put("rollsroycee", "Rolls Royce");
diccionario.put("saabb", "Saab");
diccionario.put("saturrn", "Saturn");
diccionario.put("scionn", "Scion");
diccionario.put("smartt", "Smart");
diccionario.put("srtt", "SRT");
diccionario.put("studebakerr", "Studebaker");
diccionario.put("trabantt", "Trabant");
diccionario.put("ladaa", "Lada");
diccionario.put("daewooo", "Daewoo");
diccionario.put("ssangyongg", "SsangYong");
diccionario.put("tataa", "Tata");
diccionario.put("mahindrra", "Mahindra");
diccionario.put("geelyy", "Geely");
diccionario.put("bydd", "BYD");
diccionario.put("greatwalll", "Great Wall");
diccionario.put("cheryy", "Chery");
diccionario.put("jacc", "JAC");
diccionario.put("baicc", "BAIC");
diccionario.put("dongfengg", "Dongfeng");
diccionario.put("haimaa", "Haima");
diccionario.put("jettaa", "Jetta");
diccionario.put("lynkcoo", "Lynk & Co");
diccionario.put("nioo", "NIO");
diccionario.put("xpenng", "XPeng");
diccionario.put("liauto", "Li Auto");
diccionario.put("wulinng", "Wuling");

// 🔹 MODELOS DE VEHÍCULOS (100+)
diccionario.put("corollaa", "Corolla");
diccionario.put("camryy", "Camry");
diccionario.put("rav44", "RAV4");
diccionario.put("hiluxx", "Hilux");
diccionario.put("priuss", "Prius");
diccionario.put("yariss", "Yaris");
diccionario.put("tacomaa", "Tacoma");
diccionario.put("tundrra", "Tundra");
diccionario.put("highlander", "Highlander");
diccionario.put("focus", "Focus");
diccionario.put("fussion", "Fusion");
diccionario.put("escape", "Escape");
diccionario.put("explorer", "Explorer");
diccionario.put("f150", "F-150");
diccionario.put("mustang", "Mustang");
diccionario.put("ranger", "Ranger");
diccionario.put("civic", "Civic");
diccionario.put("accord", "Accord");
diccionario.put("crv", "CR-V");
diccionario.put("pilot", "Pilot");
diccionario.put("sentra", "Sentra");
diccionario.put("altima", "Altima");
diccionario.put("maxima", "Maxima");
diccionario.put("rogue", "Rogue");
diccionario.put("xtrail", "X-Trail");
diccionario.put("golf", "Golf");
diccionario.put("jetta", "Jetta");
diccionario.put("passat", "Passat");
diccionario.put("tiguan", "Tiguan");
diccionario.put("polo", "Polo");
diccionario.put("tucson", "Tucson");
diccionario.put("santa", "Santa Fe");
diccionario.put("elantra", "Elantra");
diccionario.put("accent", "Accent");
diccionario.put("sportage", "Sportage");
diccionario.put("sorento", "Sorento");
diccionario.put("rio", "Rio");
diccionario.put("serie3", "Serie 3");
diccionario.put("serie5", "Serie 5");
diccionario.put("x5", "X5");
diccionario.put("x3", "X3");
diccionario.put("clasec", "Clase C");
diccionario.put("clasee", "Clase E");
diccionario.put("clases", "Clase S");
diccionario.put("a4", "A4");
diccionario.put("a6", "A6");
diccionario.put("q5", "Q5");
diccionario.put("q7", "Q7");
diccionario.put("clio", "Clio");
diccionario.put("megane", "Megane");
diccionario.put("captur", "Captur");
diccionario.put("kadjar", "Kadjar");
diccionario.put("208", "208");
diccionario.put("308", "308");
diccionario.put("3008", "3008");
diccionario.put("5008", "5008");
diccionario.put("c3", "C3");
diccionario.put("c4", "C4");
diccionario.put("berlingo", "Berlingo");
diccionario.put("panda", "Panda");
diccionario.put("punto", "Punto");
diccionario.put("tipo", "Tipo");
diccionario.put("astra", "Astra");
diccionario.put("corsa", "Corsa");
diccionario.put("insignia", "Insignia");
diccionario.put("impreza", "Impreza");
diccionario.put("forester", "Forester");
diccionario.put("outback", "Outback");
diccionario.put("cx5", "CX-5");
diccionario.put("cx9", "CX-9");
diccionario.put("mazda3", "Mazda 3");
diccionario.put("mazda6", "Mazda 6");
diccionario.put("lancer", "Lancer");
diccionario.put("outlander", "Outlander");
diccionario.put("pajero", "Pajero");
diccionario.put("models", "Model S");
diccionario.put("model3", "Model 3");
diccionario.put("modely", "Model Y");
diccionario.put("modelx", "Model X");
diccionario.put("s60", "S60");
diccionario.put("s90", "S90");
diccionario.put("xc60", "XC60");
diccionario.put("xc90", "XC90");
diccionario.put("xf", "XF");
diccionario.put("xe", "XE");
diccionario.put("fpace", "F-Pace");
diccionario.put("rangerover", "Range Rover");
diccionario.put("discovery", "Discovery");
diccionario.put("defender", "Defender");
diccionario.put("cayenne", "Cayenne");
diccionario.put("panamera", "Panamera");
diccionario.put("911", "911");
diccionario.put("swift", "Swift");
diccionario.put("vitara", "Vitara");
diccionario.put("jimny", "Jimny");
diccionario.put("octavia", "Octavia");
diccionario.put("fabia", "Fabia");
diccionario.put("superb", "Superb");
diccionario.put("ibiza", "Ibiza");
diccionario.put("leon", "Leon");

// 🔹 APELLIDOS (100+)
diccionario.put("garciaa", "García");
diccionario.put("rodriguez", "Rodríguez");
diccionario.put("gonzalezz", "González");
diccionario.put("fernandez", "Fernández");
diccionario.put("lopez", "López");
diccionario.put("martinez", "Martínez");
diccionario.put("sanchez", "Sánchez");
diccionario.put("perez", "Pérez");
diccionario.put("gomez", "Gómez");
diccionario.put("martin", "Martín");
diccionario.put("jimenez", "Jiménez");
diccionario.put("ruiz", "Ruiz");
diccionario.put("hernandez", "Hernández");
diccionario.put("diaz", "Díaz");
diccionario.put("moreno", "Moreno");
diccionario.put("muñoz", "Muñoz");
diccionario.put("alvarez", "Álvarez");
diccionario.put("romero", "Romero");
diccionario.put("alonso", "Alonso");
diccionario.put("gutierrez", "Gutiérrez");
diccionario.put("navarro", "Navarro");
diccionario.put("torres", "Torres");
diccionario.put("dominguez", "Dominguez");
diccionario.put("vazquez", "Vázquez");
diccionario.put("ramos", "Ramos");
diccionario.put("gil", "Gil");
diccionario.put("ramirez", "Ramírez");
diccionario.put("serrano", "Serrano");
diccionario.put("blanco", "Blanco");
diccionario.put("molina", "Molina");
diccionario.put("morales", "Morales");
diccionario.put("suarez", "Suárez");
diccionario.put("ortega", "Ortega");
diccionario.put("delgado", "Delgado");
diccionario.put("castro", "Castro");
diccionario.put("ortiz", "Ortiz");
diccionario.put("rubio", "Rubio");
diccionario.put("marin", "Marín");
diccionario.put("sanz", "Sanz");
diccionario.put("nuñez", "Nuñez");
diccionario.put("iglesias", "Iglesias");
diccionario.put("medina", "Medina");
diccionario.put("garrido", "Garrido");
diccionario.put("cortes", "Cortés");
diccionario.put("castillo", "Castillo");
diccionario.put("santos", "Santos");
diccionario.put("losa", "Losa");

// 🔹 PALABRAS DE ALMACÉN (100+)
diccionario.put("inventarioo", "inventario");
diccionario.put("stockk", "stock");
diccionario.put("almacenn", "almacén");
diccionario.put("bodegaa", "bodega");
diccionario.put("deposito", "depósito");
diccionario.put("estante", "estante");
diccionario.put("anaquel", "anaquel");
diccionario.put("rackk", "rack");
diccionario.put("gondola", "góndola");
diccionario.put("ubicacion", "ubicación");
diccionario.put("posicion", "posición");
diccionario.put("localizacion", "localización");
diccionario.put("codigo", "código");
diccionario.put("sku", "SKU");
diccionario.put("lote", "lote");
diccionario.put("serie", "serie");
diccionario.put("caducidad", "caducidad");
diccionario.put("vencimiento", "vencimiento");
diccionario.put("existencias", "existencias");
diccionario.put("cantidad", "cantidad");
diccionario.put("unidades", "unidades");
diccionario.put("piezas", "piezas");
diccionario.put("entrada", "entrada");
diccionario.put("salida", "salida");
diccionario.put("ingreso", "ingreso");
diccionario.put("egreso", "egreso");
diccionario.put("movimiento", "movimiento");
diccionario.put("traslado", "traslado");
diccionario.put("transferencia", "transferencia");
diccionario.put("recepcion", "recepción");
diccionario.put("despacho", "despacho");
diccionario.put("envio", "envío");
diccionario.put("pedido", "pedido");
diccionario.put("orden", "orden");
diccionario.put("compra", "compra");
diccionario.put("venta", "venta");
diccionario.put("proveedor", "proveedor");
diccionario.put("cliente", "cliente");
diccionario.put("distribuidor", "distribuidor");
diccionario.put("fabricante", "fabricante");
diccionario.put("marca", "marca");
diccionario.put("modelo", "modelo");
diccionario.put("categoria", "categoría");
diccionario.put("familia", "familia");
diccionario.put("subfamilia", "subfamilia");
diccionario.put("grupo", "grupo");
diccionario.put("subgrupo", "subgrupo");
diccionario.put("clasificacion", "clasificación");
diccionario.put("etiqueta", "etiqueta");
diccionario.put("rotulo", "rótulo");
diccionario.put("codigobarras", "código de barras");
diccionario.put("qr", "QR");
diccionario.put("escaneo", "escaneo");
diccionario.put("lector", "lector");
diccionario.put("terminal", "terminal");
diccionario.put("handheld", "handheld");
diccionario.put("scanner", "scanner");
diccionario.put("conteo", "conteo");
diccionario.put("inventariar", "inventariar");
diccionario.put("contar", "contar");
diccionario.put("verificar", "verificar");
diccionario.put("auditar", "auditar");
diccionario.put("ajustar", "ajustar");
diccionario.put("diferencia", "diferencia");
diccionario.put("sobrante", "sobrante");
diccionario.put("faltante", "faltante");
diccionario.put("merma", "merma");
diccionario.put("perdida", "pérdida");
diccionario.put("daniado", "dañado");
diccionario.put("defectuoso", "defectuoso");
diccionario.put("devolucion", "devolución");
diccionario.put("garantia", "garantía");
diccionario.put("reclamo", "reclamo");
diccionario.put("reposicion", "reposición");
diccionario.put("surtir", "surtir");
diccionario.put("abastecer", "abastecer");
diccionario.put("reabastecer", "reabastecer");
diccionario.put("reorden", "reorden");
diccionario.put("stockminimo", "stock mínimo");
diccionario.put("stockmaximo", "stock máximo");
diccionario.put("puntopedido", "punto de pedido");
diccionario.put("leadtime", "lead time");
diccionario.put("rotacion", "rotación");
diccionario.put("indice", "índice");
diccionario.put("kpi", "KPI");
diccionario.put("metricas", "métricas");
diccionario.put("reporte", "reporte");
diccionario.put("informe", "informe");
diccionario.put("dashboard", "dashboard");
diccionario.put("indicador", "indicador");

// 🔹 MÁS AUTO PARTES (100+)
diccionario.put("inyectorr", "inyector");
diccionario.put("bombacombustible", "bomba de combustible");
diccionario.put("filtrocombustible", "filtro de combustible");
diccionario.put("filtroaire", "filtro de aire");
diccionario.put("filtroaceite", "filtro de aceite");
diccionario.put("filtrohabitaculo", "filtro de habitáculo");
diccionario.put("correadistribucion", "correa de distribución");
diccionario.put("cadenadistribucion", "cadena de distribución");
diccionario.put("tensorr", "tensor");
diccionario.put("poleaa", "polea");
diccionario.put("discoembrague", "disco de embrague");
diccionario.put("collarin", "collarín");
diccionario.put("presionembrague", "presión de embrague");
diccionario.put("bombaembrague", "bomba de embrague");
diccionario.put("cilindroo", "cilindro");
diccionario.put("cigueñal", "cigüeñal");
diccionario.put("pistonn", "pistón");
diccionario.put("bielaa", "biela");
diccionario.put("arbollebas", "árbol de levas");
diccionario.put("taquee", "taqué");
diccionario.put("levaa", "leva");
diccionario.put("retenn", "retén");
diccionario.put("juntaculata", "junta de culata");
diccionario.put("juntatapon", "junta de tapón");
diccionario.put("juntahomocinetica", "junta homocinética");
diccionario.put("sensorrpm", "sensor de RPM");
diccionario.put("sensoroxigeno", "sensor de oxígeno");
diccionario.put("sensortemperatura", "sensor de temperatura");
diccionario.put("sensorpresion", "sensor de presión");
diccionario.put("sensorposicion", "sensor de posición");
diccionario.put("sensorvelocidad", "sensor de velocidad");
diccionario.put("sensordetonacion", "sensor de detonación");
diccionario.put("sensorabs", "sensor ABS");
diccionario.put("sensormap", "sensor MAP");
diccionario.put("sensormaf", "sensor MAF");
diccionario.put("actuadorr", "actuador");
diccionario.put("valvulaiac", "válvula IAC");
diccionario.put("valvulaegr", "válvula EGR");
diccionario.put("valvulapcv", "válvula PCV");
diccionario.put("wastegate", "wastegate");
diccionario.put("blowoff", "válvula blow-off");
diccionario.put("bombaagua", "bomba de agua");
diccionario.put("abrazadera", "abrazadera");
diccionario.put("correaa", "correa");
diccionario.put("arranquee", "arranque");
diccionario.put("reguladorr", "regulador");
diccionario.put("escobillaa", "escobilla");
diccionario.put("rotorr", "rotor");
diccionario.put("estatorr", "estator");
diccionario.put("tapadistribuidor", "tapa del distribuidor");
diccionario.put("rotordistribuidor", "rotor del distribuidor");
diccionario.put("cablebujia", "cable de bujía");
diccionario.put("moduloencendido", "módulo de encendido");
diccionario.put("cajafusibles", "caja de fusibles");
diccionario.put("centralita", "centralita");
diccionario.put("bcm", "módulo de control corporal");
diccionario.put("tcm", "módulo de control de transmisión");
diccionario.put("esp", "control de estabilidad");
diccionario.put("pretensorr", "pretensor");
diccionario.put("sensorimpacto", "sensor de impacto");
diccionario.put("moduloairbag", "módulo de airbag");
diccionario.put("cuadroo", "cuadro de instrumentos");
diccionario.put("velocimetro", "velocímetro");
diccionario.put("tacometro", "tacómetro");
diccionario.put("testigoo", "testigo");
diccionario.put("piloto", "piloto");
diccionario.put("antiniebla", "antiniebla");
diccionario.put("intermitente", "intermitente");
diccionario.put("stop", "luz de stop");
diccionario.put("marcha", "luz de marcha atrás");
diccionario.put("plafon", "plafón");
diccionario.put("dome", "luz dome");
diccionario.put("bombilla", "bombilla");
diccionario.put("halogena", "halógena");
diccionario.put("xenon", "xenón");
diccionario.put("laser", "láser");
diccionario.put("proyector", "proyector");
diccionario.put("optica", "óptica");
diccionario.put("lente", "lente");
diccionario.put("reflector", "reflector");

// 🔹 MÁS NOMBRES MASCULINOS (50+)
diccionario.put("eduarddo", "Eduardo");
diccionario.put("alfonzo", "Alfonso");
diccionario.put("ramonn", "Ramón");
diccionario.put("raulll", "Raúl");
diccionario.put("sergioo", "Sergio");
diccionario.put("victorr", "Víctor");
diccionario.put("pabloo", "Pablo");
diccionario.put("ignacioo", "Ignacio");
diccionario.put("oscarr", "Óscar");
diccionario.put("rubenn", "Rubén");
diccionario.put("samuell", "Samuel");
diccionario.put("tomass", "Tomás");
diccionario.put("daniel", "Daniel");
diccionario.put("emilioo", "Emilio");
diccionario.put("enrique", "Enrique");
diccionario.put("felippe", "Felipe");
diccionario.put("guillermo", "Guillermo");
diccionario.put("hecttor", "Héctor");
diccionario.put("hugoo", "Hugo");
diccionario.put("ivann", "Iván");
diccionario.put("joaquin", "Joaquín");
diccionario.put("juliio", "Julio");
diccionario.put("leonardo", "Leonardo");
diccionario.put("marcoss", "Marcos");
diccionario.put("mariio", "Mario");
diccionario.put("matias", "Matías");
diccionario.put("nicolas", "Nicolás");
diccionario.put("octavio", "Octavio");
diccionario.put("rodrigo", "Rodrigo");
diccionario.put("salvador", "Salvador");
diccionario.put("sebastian", "Sebastián");
diccionario.put("simonn", "Simón");
diccionario.put("ulises", "Ulises");
diccionario.put("valentin", "Valentín");
diccionario.put("xavier", "Xavier");

// 🔹 MÁS NOMBRES FEMENINOS (50+)
diccionario.put("elenaa", "Elena");
diccionario.put("cristina", "Cristina");
diccionario.put("teresa", "Teresa");
diccionario.put("silvia", "Silvia");
diccionario.put("raquel", "Raquel");
diccionario.put("pilar", "Pilar");
diccionario.put("concepcion", "Concepción");
diccionario.put("dolores", "Dolores");
diccionario.put("angeles", "Ángeles");
diccionario.put("consuelo", "Consuelo");
diccionario.put("esperanza", "Esperanza");
diccionario.put("fe", "Fe");
diccionario.put("luz", "Luz");
diccionario.put("mar", "Mar");
diccionario.put("sol", "Sol");
diccionario.put("estrella", "Estrella");
diccionario.put("flor", "Flor");
diccionario.put("rosa", "Rosa");
diccionario.put("lilia", "Lilia");
diccionario.put("margarita", "Margarita");
diccionario.put("violeta", "Violeta");
diccionario.put("iris", "Iris");
diccionario.put("jazmin", "Jazmín");
diccionario.put("dalila", "Dalila");
diccionario.put("eva", "Eva");
diccionario.put("lidia", "Lidia");
diccionario.put("marta", "Marta");
diccionario.put("miriam", "Miriam");
diccionario.put("noemi", "Noemí");
diccionario.put("rebeca", "Rebeca");
diccionario.put("sara", "Sara");
diccionario.put("susana", "Susana");
diccionario.put("veronica", "Verónica");
diccionario.put("yolanda", "Yolanda");
diccionario.put("zoe", "Zoe");
diccionario.put("adriana", "Adriana");
diccionario.put("beatriz", "Beatriz");
diccionario.put("carolina", "Carolina");
diccionario.put("diana", "Diana");
diccionario.put("elena", "Elena");
diccionario.put("fabiola", "Fabiola");
diccionario.put("gloria", "Gloria");
diccionario.put("ines", "Inés");
diccionario.put("jimena", "Jimena");
diccionario.put("karen", "Karen");
diccionario.put("lucia", "Lucía");
diccionario.put("natalia", "Natalia");
diccionario.put("olivia", "Olivia");
diccionario.put("priscila", "Priscila");
diccionario.put("regina", "Regina");

// 🔹 TÉRMINOS DE TALLER MECÁNICO (50+)
diccionario.put("tallerr", "taller");
diccionario.put("mecanico", "mecánico");
diccionario.put("tecnico", "técnico");
diccionario.put("diagnostico", "diagnóstico");
diccionario.put("reparacion", "reparación");
diccionario.put("mantenimiento", "mantenimiento");
diccionario.put("service", "servicio");
diccionario.put("revision", "revisión");
diccionario.put("inspeccion", "inspección");
diccionario.put("calibracion", "calibración");
diccionario.put("ajuste", "ajuste");
diccionario.put("reglaje", "reglaje");
diccionario.put("sincronizacion", "sincronización");
diccionario.put("balanceo", "balanceo");
diccionario.put("alineacion", "alineación");
diccionario.put("rotacion", "rotación");
diccionario.put("presion", "presión");
diccionario.put("temperatura", "temperatura");
diccionario.put("voltaje", "voltaje");
diccionario.put("amperaje", "amperaje");
diccionario.put("resistencia", "resistencia");
diccionario.put("continuidad", "continuidad");
diccionario.put("cortocircuito", "cortocircuito");
diccionario.put("senal", "señal");
diccionario.put("frecuencia", "frecuencia");
diccionario.put("potencia", "potencia");
diccionario.put("par", "par motor");
diccionario.put("cilindrada", "cilindrada");
diccionario.put("compresion", "compresión");
diccionario.put("vacío", "vacío");
diccionario.put("estanqueidad", "estanqueidad");
diccionario.put("fugaaceite", "fuga de aceite");
diccionario.put("fugaagua", "fuga de agua");
diccionario.put("fugaaire", "fuga de aire");
diccionario.put("consumo", "consumo");
diccionario.put("rendimiento", "rendimiento");
diccionario.put("eficiencia", "eficiencia");
diccionario.put("emisiones", "emisiones");
diccionario.put("contaminacion", "contaminación");
diccionario.put("normativa", "normativa");
diccionario.put("homologacion", "homologación");
diccionario.put("certificacion", "certificación");
diccionario.put("garantia", "garantía");
diccionario.put("seguro", "seguro");
diccionario.put("itv", "ITV");
diccionario.put("mot", "MOT");
diccionario.put("revisiontecnica", "revisión técnica");
diccionario.put("Adrian", "Adrián");
diccionario.put("Avila", "Ávila");

    
    }

    public static void aplicar(JTextComponent campo) {
        campo.setDocument(new AutoCorrectorDocument(campo));
    }

    private static class AutoCorrectorDocument extends PlainDocument {
        private final JTextComponent campo;
        private final JPopupMenu popup = new JPopupMenu();
        private final java.util.List<JMenuItem> items = new ArrayList<>();
        private int selectedIndex = -1;
        private javax.swing.Timer sugerenciaTimer;
        private boolean procesandoCambio = false;

        public AutoCorrectorDocument(JTextComponent campo) {
            this.campo = campo;
            configurarTimer();
            configurarEventosTeclado();
            configurarPopup();
        }

        private void configurarTimer() {
            sugerenciaTimer = new javax.swing.Timer(500, e -> mostrarSugerencias());
            sugerenciaTimer.setRepeats(false);
        }

        private void configurarPopup() {
            popup.setFocusable(false);
        }

        private void configurarEventosTeclado() {
            campo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (popup.isVisible()) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_DOWN:
                                moverSeleccion(1);
                                e.consume();
                                break;
                            case KeyEvent.VK_UP:
                                moverSeleccion(-1);
                                e.consume();
                                break;
                            case KeyEvent.VK_ENTER:
                                if (selectedIndex >= 0 && selectedIndex < items.size()) {
                                    items.get(selectedIndex).doClick();
                                    e.consume();
                                }
                                break;
                            case KeyEvent.VK_ESCAPE:
                                popup.setVisible(false);
                                e.consume();
                                break;
                        }
                    }
                    
                    // Iniciar timer con cualquier tecla que no sea de navegación
                    if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && 
                        e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
                        if (sugerenciaTimer.isRunning()) {
                            sugerenciaTimer.restart();
                        } else {
                            sugerenciaTimer.start();
                        }
                    }
                }
            });

            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    popup.setVisible(false);
                }
            });
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (procesandoCambio) {
                super.insertString(offs, str, a);
                return;
            }

            super.insertString(offs, str, a);

            // Autocorrección con espacio
            if (" ".equals(str)) {
                autocorregir();
            }
        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            if (procesandoCambio) {
                super.remove(offs, len);
                return;
            }
            super.remove(offs, len);
        }

        private void autocorregir() {
            try {
                procesandoCambio = true;
                
                String texto = getText(0, getLength());
                String[] palabras = texto.split("\\s+");
                if (palabras.length == 0) return;

                String ultima = palabras[palabras.length - 1].toLowerCase();

                if (diccionario.containsKey(ultima)) {
                    String correccion = diccionario.get(ultima);
                    int start = texto.lastIndexOf(ultima);
                    if (start >= 0) {
                        replace(start, ultima.length(), correccion, null);
                    }
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            } finally {
                procesandoCambio = false;
            }
        }

        private void mostrarSugerencias() {
            if (procesandoCambio || !campo.isEnabled() || !campo.isVisible()) {
                return;
            }

            try {
                String textoCompleto = getText(0, getLength());
                if (textoCompleto.isEmpty()) {
                    popup.setVisible(false);
                    return;
                }

                String palabraActual = obtenerPalabraActual(textoCompleto);
             
                if (palabraActual.isEmpty() || palabraActual.length() < 1) {
                    popup.setVisible(false);
                    return;
                }

                // Buscar sugerencias
                java.util.List<String> sugerencias = new ArrayList<>();
                for (Map.Entry<String, String> entry : diccionario.entrySet()) {
                    String clave = entry.getKey().toLowerCase();
                    String valor = entry.getValue();
                    
                    if (clave.startsWith(palabraActual.toLowerCase()) || 
                        valor.toLowerCase().startsWith(palabraActual.toLowerCase())) {
                        if (!sugerencias.contains(valor)) {
                            sugerencias.add(valor);
                            if (sugerencias.size() >= 5) break;
                        }
                    }
                }

               

                if (sugerencias.isEmpty()) {
                    popup.setVisible(false);
                    return;
                }

                // Actualizar popup
                popup.removeAll();
                items.clear();
                selectedIndex = -1;

                for (String sugerencia : sugerencias) {
                    JMenuItem item = new JMenuItem(sugerencia);
                    item.addActionListener(e -> aplicarSugerencia(palabraActual, sugerencia));
                    
                    // Mejorar la apariencia
                    item.setBackground(Color.WHITE);
                    item.setForeground(Color.BLACK);
                    item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    
                    popup.add(item);
                    items.add(item);
                }

                // Mostrar popup en la posición correcta
                mostrarPopupEnPosicionCorrecta();

            } catch (Exception e) {
                e.printStackTrace();
                popup.setVisible(false);
            }
        }

        private void mostrarPopupEnPosicionCorrecta() {
            try {
                int posicionCaret = campo.getCaretPosition();
                Rectangle rect = campo.modelToView(posicionCaret);
                
                if (rect != null) {
                    Point location = new Point(rect.x, rect.y + rect.height);
                    
                    // Convertir a coordenadas de la pantalla
                    SwingUtilities.convertPointToScreen(location, campo);
                    
                    // Verificar que la ubicación sea válida
                    if (location.x < 0) location.x = 0;
                    if (location.y < 0) location.y = 0;
                    
                    popup.setVisible(false); // Ocultar primero
                    popup.pack(); // Ajustar tamaño
                    popup.setLocation(location);
                    popup.setVisible(true);
                    
                  
                } else {
                    // Fallback: mostrar cerca del campo
                    Point fieldLocation = campo.getLocationOnScreen();
                    popup.setLocation(fieldLocation.x, fieldLocation.y + campo.getHeight());
                    popup.setVisible(true);
                }
            } catch (BadLocationException e) {
                // Fallback simple
                popup.setLocation(campo.getLocationOnScreen().x, 
                                campo.getLocationOnScreen().y + campo.getHeight());
                popup.setVisible(true);
            }
        }

        private String obtenerPalabraActual(String texto) {
            int pos = campo.getCaretPosition();
            if (pos == 0) return "";
            
            // Buscar inicio de la palabra actual
            int inicio = pos - 1;
            while (inicio >= 0 && !Character.isWhitespace(texto.charAt(inicio))) {
                inicio--;
            }
            inicio++; // Ajustar al primer carácter
            
            // Buscar fin de la palabra actual
            int fin = pos;
            while (fin < texto.length() && !Character.isWhitespace(texto.charAt(fin))) {
                fin++;
            }
            
            return texto.substring(inicio, fin);
        }

        private void aplicarSugerencia(String palabraActual, String sugerencia) {
            try {
                procesandoCambio = true;
                
                String texto = getText(0, getLength());
                int start = texto.lastIndexOf(palabraActual);
                
                if (start >= 0) {
                    replace(start, palabraActual.length(), sugerencia, null);
                }
                
                popup.setVisible(false);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            } finally {
                procesandoCambio = false;
            }
        }

        private void moverSeleccion(int dir) {
            if (items.isEmpty()) return;

            // Quitar selección anterior
            if (selectedIndex >= 0 && selectedIndex < items.size()) {
                items.get(selectedIndex).setBackground(Color.WHITE);
                items.get(selectedIndex).setForeground(Color.BLACK);
            }

            selectedIndex += dir;
            if (selectedIndex < 0) selectedIndex = items.size() - 1;
            if (selectedIndex >= items.size()) selectedIndex = 0;

            // Aplicar nueva selección
            if (selectedIndex >= 0 && selectedIndex < items.size()) {
                items.get(selectedIndex).setBackground(Color.BLUE);
                items.get(selectedIndex).setForeground(Color.WHITE);
            }
        }
    }
}