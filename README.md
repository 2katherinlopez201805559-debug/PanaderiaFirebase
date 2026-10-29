# 🥖 Panadería San Carlos — versión Firebase

Proyecto final de **Desarrollo Web** — Universidad Mariano Gálvez de Guatemala,
Ingeniería en Sistemas de Información, Plan Sábado (2026). Katherin López · Carné 3675.

Misma aplicación que la versión con Oracle (mismas páginas, mismas rutas REST, misma
plantilla Jersey), pero los datos se guardan en **Firebase Cloud Firestore**.
Funciona en computadora y en teléfono, y se puede **instalar como app**.

| Capa | Versión Oracle | Esta versión |
|---|---|---|
| Frontend | HTML, CSS, JS | Igual + app instalable (manifest y service worker) |
| Backend | Java 8, Jersey 2.26, JWT | Igual (plantilla Jersey) |
| Datos | Oracle: tablas + paquetes PL/SQL | Firestore: colecciones + reglas en los *managers* Java |
| Transacciones | COMMIT / ROLLBACK | `db.runTransaction(...)`: todo o nada |
| Conexión | `ojdbc` + `config.xml <db>` | `firebase-admin` + `config.xml <firebase>` |

## Colecciones de Firestore

| Colección | Documento | Equivale a |
|---|---|---|
| `insumos` | `1`, `2`… | tabla INSUMO |
| `productos` | `1`, `2`… (con su **RECETA** adentro) | PRODUCTO + RECETA_DETALLE |
| `sucursales` | `1`…`5` | SUCURSAL |
| `usuarios` | `kgalvez`, `chiva`… (clave en SHA-256) | USUARIO |
| `pedidos` | `1`, `2`… (con su **DETALLE** adentro) | PEDIDO + PEDIDO_DETALLE |
| `devoluciones` | `1`, `2`… | DEVOLUCION |
| `pagos` | `1`, `2`… | PAGO |
| `contadores` | uno por colección | GENERATED AS IDENTITY |
| `sistema/inicial` | marca de datos cargados | — |

La primera vez que el backend se conecta, si la base está vacía, carga solo los datos
iniciales: 10 insumos, 9 panes con receta, 5 sucursales y 6 usuarios
(contraseña de todos: `Panaderia2026`).

## Puesta en marcha

1. **Firebase:** en https://console.firebase.google.com cree un proyecto, luego
   *Compilación → Firestore Database → Crear base de datos* (modo producción).
2. **Reglas:** en *Firestore → Reglas* pegue el contenido de `firestore.rules` y publique.
3. **Clave:** *Configuración del proyecto → Cuentas de servicio → Generar nueva clave privada*.
   Guarde el archivo como `clave-firebase.json` en esta carpeta (NO se sube a GitHub).
4. **config.xml** (`src/main/resources`): ponga la ruta de la clave y el ID del proyecto.
5. **NetBeans:** abrir el proyecto → *Clean and Build* → *Run*.
   Abrir `http://localhost:8080/PanaderiaFirebase/`.

## Teléfono

- Misma red wifi: `http://IP-DE-LA-PC:8080/PanaderiaFirebase/`.
- Para instalarla como app el navegador exige **https** (por ejemplo con un túnel o al
  publicarla): en Android aparece el botón **📲 Instalar app**; en iPhone,
  Safari → Compartir → *Agregar a pantalla de inicio*.
