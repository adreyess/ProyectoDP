# E-Bike Tour — Entrega inicial

Proyecto de **Desarrollo de Programas** · Curso 2026/2027 · Grado en Ingeniería en Informática

---

## 1. Miembros del grupo

**Adrián Reyes Sosa**.

## 2. Clase que se ejecuta para realizar la simulación

La simulación se lanza con la clase **`EBikeDemo`**, invocando su método `main()`:

1. Abrir esta carpeta como proyecto en **BlueJ 5.2.0 o posterior** (Java 11 o posterior).
2. Pulsar **Compilar**.
3. Clic derecho sobre la clase `EBikeDemo` → `void main()`.

La simulación muestra por consola la información inicial, los 50 turnos de simulación y la
información final.

### Escenario de datos

El escenario se elige con la constante `DEMO` de la clase `EBikeDemo`:

```java
private static final DemoType DEMO = DemoType.SIMPLE;
```

Valores posibles: `DemoType.SIMPLE`, `DemoType.MEDIUM` y `DemoType.ADVANCED`. La entrega se ha
generado con los tres; por defecto queda en `SIMPLE`. En cada escenario se crean 4 estaciones con
4 docks cada una; lo único que cambia es el número de bicicletas (2, 5 y 8 respectivamente).

### Salidas por pantalla incluidas

| Fichero | Escenario | Líneas |
|---|---|---|
| `SimpleOutput.txt` | SIMPLE (2 bicis) | 176 |
| `MediumOutput.txt` | MEDIUM (5 bicis) | 338 |
| `AdvancedOutput.txt` | ADVANCED (8 bicis) | 497 |

La salida por consola de los tres escenarios coincide con la de los ficheros de referencia del
enunciado.

Para respetar las instrucciones de entrega, se pueden comprobar las salidas generadas
de este proyecto en el repositorio de GitHub, organizadas en la carpeta outputs.

## 3. Extras del proyecto

### Comparadores propios (7 clases)

`ComparatorEBIdBike`, `ComparatorEBArrivalStep`, `ComparatorBikeStationId`,
`ComparatorBikeStationNumberRecharged`, `ComparatorDockChargingSpeed`,
`ComparatorDockChargingFee` y `ComparatorDocksId`.

Se usan con `Collections.sort` para mantener ordenadas la flota de la compañía, la red de
estaciones y los docks de cada estación. Cada comparador aplica solo su criterio principal y los
desempates (tarifa e identificador) los resuelve el orden estable de `Collections.sort` al
encadenar comparadores, tal como pide el enunciado.

### Uso de Collections

`List` y `ArrayList` en `EBCompany`, `BikeStation`, `Dock` y `EBikeDemo`, con `Collections.sort` y
con getters que devuelven **copias** de las listas para no exponer la estructura interna.

### Uso de Iterator (3 veces)

Recorrido de las estaciones para buscar una estación por identificador y por localización
(`EBCompany`) y recorrido de los docks para buscar el primero libre (`BikeStation`).

### Pruebas unitarias con JUnit 4

6 clases de prueba y **80 métodos de test**, todos con fixtures (`@Before` / `@After`):

| Clase | Tests |
|---|---|
| `LocationTest` | 12 |
| `DockTest` | 10 |
| `BikeStationTest` | 9 |
| `ElectricBikeTest` | 29 |
| `EBCompanyTest` | 10 |
| `ComparatorsTest` | 10 |

Cubren los métodos pedidos en el enunciado (`testDistance`, `testAdjacentLocations`,
`testRecharge`, `testCreation`, `testEnoughBattery`, `testCalculateRoute`, `testAddDock`) y el
resto de métodos públicos que devuelven un valor o modifican el estado del objeto.

### Documentación JavaDoc

Documentación completa (clases, campos, métodos, `@param`, `@return` y `@throws`) en todas las
clases del proyecto, incluidas las de prueba. Verificado con `javadoc -Xdoclint:all` sin errores.

### Control de errores

Los constructores y los métodos que reciben parámetros validan los valores nulos y las coordenadas
fuera de rango, lanzando `NullPointerException` o `IllegalArgumentException`.

### Repositorio Git

<https://github.com/adreyess/ProyectoDP>
