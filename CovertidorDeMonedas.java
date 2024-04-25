/**En esta parte agregamos todo lo que necesitamos para poder correr nuestro programa, como NOTA importante
 * se debe tener agregado el Gson, el que utilice gue el de google que anexaré en los archivos necesarios
 * para poder correr el archivo sin problemas
 * **/


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CovertidorDeMonedas {

/** En esta parte del código es donde realizamos la busqueda del API y realizamos el proceso para extraer los datos
 * que necesitamos para poder generar buestra variable tipoDeCambio y poder aplicarlo en nuestras operaciones según
 * solicite el usuario, comencé con dos pero a lo largo de la práctica iré realizando modificaciones**/
    private static double obtenerTipoCambio() throws IOException {
        URL url = new URL("https://api.exchangerate-api.com/v4/latest/USD");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject ratesObject = jsonObject.getAsJsonObject("rates");
        JsonElement mxnRateElement = ratesObject.get("MXN");
        return mxnRateElement.getAsDouble();
    }

    public static void main(String[] args) throws IOException {
        double tipoCambio;
        try {
            tipoCambio = obtenerTipoCambio();
        } catch (IOException e) {
            tipoCambio = 20.0; // Si hay un error al obtener el tipo de cambio, se usa un valor predeterminado
            System.err.println("Error al obtener el tipo de cambio: " + e.getMessage());
            return; // Debe salir del programa si no puede obtener el tipo de cambio
        }

        Scanner teclado = new Scanner(System.in);
        int eleccionUsuario;
/**
 * En esta parte de código comienzo con una bienvenida y se muestra el menú en blucle para cualquiera de las opciones
 * tenemos el do-while para el bucle del menu y el switch para las opciones que tenemos y que podemos agregar a lo largo
 * del programa, es algo sencillo pero si costro algo de trabajo, no encapsulé por que estamos en confianza :)
 */
        do {
            String bienvenida = """
             ***********************************************************************************************************
             *                                      Bienvenid@ al convertidor de Monedas                     Ver. 1.0  *
             *                                                                                                         *
             * Por el momento tenemos estas opciones disponibles:                                                      *
             *                                                                                                         *
             *   1.- Convertir Peso Méxicano a Dolar Americano.                                                        *
             *   2.- Convertir Dolar Americano a Peso Mexicano.                                                        *
             *   3.- Salir del programa.                                                                               *
             *                                                                                           by: Orozco    *
             ***********************************************************************************************************
             """;

            System.out.println(bienvenida);
            System.out.println("Por favor digita el número acorde a tu elección:");
            eleccionUsuario = teclado.nextInt();

            switch (eleccionUsuario) {
                case 1:
                    System.out.print("Ingrese la cantidad en pesos mexicanos: ");
                    double cantidadPesos = teclado.nextDouble();
                    double dolares = cantidadPesos / tipoCambio;
                    System.out.println("La cantidad en dólares americanos es: " + dolares);
                    break;
                case 2:
                    System.out.print("Ingrese la cantidad en dólares americanos: ");
                    double dolaresAmericanos = teclado.nextDouble();
                    double conversionPeso = dolaresAmericanos * tipoCambio;
                    System.out.println("La cantidad en pesos mexicanos es: " + conversionPeso);
                    break;
                case 3:
                    System.out.println("Gracias por usar el convertidor de divisas. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (eleccionUsuario != 3);

        teclado.close(); //Este proceso lo recomiendan realizar, en varias partes que estuve estudiando lo mencionan
    }
}
