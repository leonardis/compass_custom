# compass_custom

**1. Propón los pasos, clases, layouts y recursos que utilizarías para hacer un Stepper reutilizable y que cumpla con los parámetros definidos en la guía de material design:**

https://material.io/guidelines/components/steppers.html

Los pasos pueden perderse un poco haciendolos escritos y pueden tornarse un poco enredados. Sin embargo coloco las clases, recursos y layout que considero como base para crear el Stepper lo mas reutilizable y configurable en una primera instancia.

- Clases:    
    * StepperFactory -> Una clase fabrica para determinar el tipo de stepper que se creara y los componentes de UI que utilizara.    
    * StepperVertical -> Un tipo de stepper basado en recyclerview.    StepperHorizontal -> Un tipo de stepper basado en ViewPager.    
    * StepperLayout -> Custom view para crear steppers a la medida.    StepperListener -> Interfaz que escuchara los eventos de los steppers.    
    * StepperRecyclerAdapter -> Adaptador usado para el StepperVertical.
    * StepperPagerAdapter -> Adaptador usado para el StepperHorizontal.
    
- Layout:    
  * stepper_layout -> layout que contendra toda a vista compleja de StepperLayout al ser un CompundView.    
  * stepper_text_indicator_layout -> layout que contendra el tipo de indicador basado en Texto(Pervious - Next)    
  * stepper_progress_indicator_layout -> layotu que contendra el tipo de indicador basado en un Seekbar.    
  * stepper_dots_indicator_layout -> layout que contendra el tipo de indicador basado en puntos.

- Recursos:    
  *step_indicator_fill -> Imagen para indicar el paso en el que se encuentra el stepper(Puntos, estrellas, guiones, etc).    
  * step_indicator_empty -> Imagen para indicar el paso en el que no se encuentra el stepper(Puntos, estrellas, guiones, etc).    
  * step_thumb -> Imagen para representar el thumb del SeekBar y poder hacerlo mas configurable.    
  * attrs -> Coloco este archivo ya que lo considero importante para que las CustomViews sean lo mas configurable posible des el editor de layout de Android Studio.


**2. Crea un shake action en android y pon el código.**
- Archivo adjunto: ShakeAction.kt     
- Uso: 

```kotlin
sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
sensorAccelerometer?.let {
  shakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
    override fun onShake(count: Int) {
      // Here I update the compass with a random color.
      val random = Random()
      val color = Color.argb(
        255,
        random.nextInt(256),
        random.nextInt(256),
        random.nextInt(256)
      )
      compassView.changeColor(color)
    }
  })
}
```


**3. Explica cómo organizas en base a tu experiencia un proyecto en Android utilizando MVP e implementando Clean Architecture, menciona los paquetes que utilizarías y la distribución de módulos.** 

Para una app mult-modular que utilice MVP, crearia 5 modulos base: app, presentation, domain, data y base. Sin enbargo, dependera mucho de la aplicacion. Por ejemplo; si la aplicacion estara basada en feature-modules entonces el sistema de modulos cambiara y solo necesitaria de app y base, y cada feature-module debera tener un sistema de paqueterias muy similares como: 

**di** 

**presentation**

`activities, fragments, views`

**domain**

`interfaces, interactors, repositories`

**data**

`models`


**utils**


De igual manera que este seria como punto de partida ya que el sistema de paqueteria y la arquitectura de la aplicacion se definirian de acuerdo a los requerimientos del mismo.
       

**4. Diseña un custom view de una brújula utilizando canvas y pon el código que utilizarías en esta sección.**

- Archivo adjunto: CompassView.kt    
- Uso:
```kotlin
sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
sensorOrientation?.let { sensor ->
      sensorManager.registerListener(object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit

        override fun onSensorChanged(event: SensorEvent) {
          val azimuth = event.values[0]
          compassView.updatePosition(azimuth)
        }
      }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
}
```

```xml
<com.example.bellatrixtest.CompassView
  android:id="@+id/compassView"
  android:layout_width="200dp"
  android:layout_height="200dp"
  app:layout_constraintTop_toTopOf="parent"
  app:layout_constraintBottom_toBottomOf="parent"
  app:layout_constraintStart_toStartOf="parent"
  app:layout_constraintEnd_toEndOf="parent"
  app:showGrades="true"
  app:compassColor="@color/colorAccent"
 />
 ```
