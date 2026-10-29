package BACKEND.utils;

import BACKEND.manager.DatosIniciales;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

/**
 * Conexión a Firebase (reemplaza a ConnectionsPool de Oracle).
 *
 * Lee de config.xml:
 *   <firebase>
 *     <credenciales>C:\...\clave-firebase.json</credenciales>   (cuenta de servicio)
 *     <proyecto>panaderia-san-carlos</proyecto>                  (ID del proyecto)
 *   </firebase>
 *
 * La primera vez que se conecta, si la base está vacía, carga los datos
 * iniciales (panes, insumos, recetas, sucursales y usuarios).
 */
public final class FirebaseConfig {

    private static Firestore db;

    private FirebaseConfig() {
    }

    public static synchronized Firestore db() throws Exception {
        if (db == null) {
            if (FirebaseApp.getApps().isEmpty()) {
                XMLConfiguration config = new Configurations().xml("config.xml");
                String ruta = config.getString("firebase.credenciales");
                String proyecto = config.getString("firebase.proyecto");
                if (ruta == null || ruta.trim().isEmpty()) {
                    throw new IllegalStateException("Falta <firebase><credenciales> en config.xml.");
                }
                try (InputStream clave = new FileInputStream(ruta.trim())) {
                    FirebaseOptions.Builder opciones = new FirebaseOptions.Builder()
                            .setCredentials(GoogleCredentials.fromStream(clave));
                    if (proyecto != null && !proyecto.trim().isEmpty()) {
                        opciones.setProjectId(proyecto.trim());
                    }
                    FirebaseApp.initializeApp(opciones.build());
                }
            }
            Firestore conexion = FirestoreClient.getFirestore();
            DatosIniciales.cargarSiHaceFalta(conexion);
            db = conexion;
        }
        return db;
    }
}
