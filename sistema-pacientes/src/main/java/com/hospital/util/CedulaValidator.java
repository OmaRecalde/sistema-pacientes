package com.hospital.util;

/**
 * Clase para validar cédulas de identidad ecuatorianas
 * Implementa el algoritmo de validación con dígito verificador (Módulo 10)
 *
 * Referencias:
 * - https://gist.github.com/vickoman/7800717
 * - https://www.jybaro.com/blog/cedula-de-identidad-ecuatoriana/
 */
public class CedulaValidator {

    // Coeficientes para el algoritmo de módulo 10
    private static final int[] COEFICIENTES = {2, 1, 2, 1, 2, 1, 2, 1, 2};

    /**
     * Valida una cédula ecuatoriana completa
     * @param cedula Cédula de 10 dígitos
     * @return true si la cédula es válida, false en caso contrario
     */
    public static boolean validarCedula(String cedula) {
        // Validar que no sea null o vacía
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }

        // Validar longitud exacta de 10 dígitos
        if (cedula.length() != 10) {
            return false;
        }

        // Validar que solo contenga dígitos
        if (!cedula.matches("\\d{10}")) {
            return false;
        }

        // Validar código de provincia (primeros 2 dígitos)
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (!validarProvincia(provincia)) {
            return false;
        }

        // Validar tercer dígito (debe ser menor a 6)
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) {
            return false;
        }

        // Validar dígito verificador
        return validarDigitoVerificador(cedula);
    }

    /**
     * Valida que el código de provincia sea válido
     * @param provincia Código de provincia (primeros 2 dígitos)
     * @return true si es válido
     */
    private static boolean validarProvincia(int provincia) {
        // Provincias válidas: 01 a 24, o 30 (consulados en el exterior)
        return (provincia >= 1 && provincia <= 24) || provincia == 30;
    }

    /**
     * Valida el dígito verificador usando el algoritmo de Módulo 10
     * @param cedula Cédula de 10 dígitos
     * @return true si el dígito verificador es correcto
     */
    private static boolean validarDigitoVerificador(String cedula) {
        int suma = 0;

        // Multiplicar primeros 9 dígitos por coeficientes
        for (int i = 0; i < 9; i++) {
            int digito = Integer.parseInt(cedula.substring(i, i + 1));
            int resultado = digito * COEFICIENTES[i];

            // Si el resultado es >= 10, restar 9
            if (resultado >= 10) {
                resultado -= 9;
            }

            suma += resultado;
        }

        // Calcular módulo 10
        int modulo = suma % 10;

        // Calcular dígito verificador esperado
        int digitoVerificadorEsperado;
        if (modulo == 0) {
            digitoVerificadorEsperado = 0;
        } else {
            digitoVerificadorEsperado = 10 - modulo;
        }

        // Obtener el último dígito de la cédula
        int digitoVerificadorReal = Integer.parseInt(cedula.substring(9, 10));

        // Comparar
        return digitoVerificadorEsperado == digitoVerificadorReal;
    }

    /**
     * Obtiene un mensaje de error descriptivo según la validación
     * @param cedula Cédula a validar
     * @return Mensaje de error específico o null si es válida
     */
    public static String obtenerMensajeError(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return "La cédula es requerida";
        }

        if (cedula.length() != 10) {
            return "La cédula debe tener exactamente 10 dígitos";
        }

        if (!cedula.matches("\\d{10}")) {
            return "La cédula solo debe contener números";
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (!validarProvincia(provincia)) {
            return "El código de provincia (" + String.format("%02d", provincia) + ") no es válido. Debe estar entre 01-24 o ser 30";
        }

        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) {
            return "El tercer dígito (" + tercerDigito + ") debe ser menor a 6";
        }

        if (!validarDigitoVerificador(cedula)) {
            return "El dígito verificador es incorrecto. La cédula no es válida";
        }

        return null; // Cédula válida
    }
}
