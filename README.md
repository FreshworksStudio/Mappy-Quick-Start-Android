# Mappy-Quick-Start-Android

<br><b>Welcome to Mappy.</b></br>
<br>We build geospatial platforms connecting venues to their guests. Our turnkey solutions (developer tools, including the Mappy API and Mappy SDK, for mobile apps), improve the guest experience using interactive and social custom maps. Our data analytics platform and operations dashboards are designed to inform data driven decisions.</br>
<br>Mappy SDKs provide an elegant and composable interface for mapping, geocoding, and routing.</br>
<br>The Mappy SDKs leverages the power of best in class technology, including ESRI using the ArcGIS Runtime SDKs as dependencies.</br>
<br>Terms of Service for our developer tools, data analytics platform, and mapping services portal are found [here](https://www.bemappy.io/terms).</br>

# Get started with Snow Mappy SDK on Android

Install the following:

 - Android SDK, latest version is recommended.
 - Android Studio, latest version is recommended.
 - Set up a physical Android device with Android version major or equal to 6 (Marshmallow)

If you don't have an Android project and want to try out a Mappy product, or want to see a code example, you can download or clone this sample.

# Register your App with Mappy
Your app needs to be registered with Mappy to use the Snow Mappy SDK in your app. For a quote, please email [info@beMappy.io](info@beMappy.io). Mappy will provide customers a Client Id and a secret key associated with your app. Make sure you store them securely.


# Add Snow Mappy SDK to your App

1. In your project's `build.gradle` file (the one in the root directory), make sure you have the following maven repositories defined in the `repositories` block:

>The SDK is dependant on a special toolkit dependency stored in Jfrog.
```
repositories {
        mavenCentral()
        maven {
            url 'https://esri.jfrog.io/artifactory/arcgis'
        }
    }
```

2. In your module's `build.gradle` file (usually named `app/build.gradle`), add the library dependency in the `dependencies` block:

   [![Maven Central](https://img.shields.io/maven-central/v/io.github.bemappy/mappy.svg?label=Latest%20Version)](https://search.maven.org/artifact/io.github.bemappy/mappy)

```
dependencies {
    implement("io.github.bemappy:mappy:0.10.10")
}
```

# Initialize Mappy in Your App

Mappy needs to be initialized in your app by adding an initialization code.

```kotlin
val mappy = Mappy.createInstance(applicationContext)

//Using Kotlin Coroutines:
CoroutineScope(Dispatchers.IO).launch {
    mappy.initialize(<ClientId>, <ClientSecret>)
}

//Using Callback:
mappy.initialize(<ClientId>, <ClientSecret>, object: CompletionCallback<Void?> {
    override fun onSuccess(result: Void?) {
        //Initialization Success
    }

    override fun onError(throwable: Throwable) {
        //Initialization Error
    }
})
```

```java
Mappy mappy = Mappy.createInstance(getApplicationContext());

mappy.initialize(<ClientId>, <ClientSecret>, new CompletionCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        //Initialization Success
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        //Initialization Error
    }
});
```

> Make sure to replace `ClientId` and `ClientSecret` with your credentials.

1. Import the Mappy module.
2. Create a Mappy object instance through the code instruction displayed in the example, be sure to pass a valid context to the createInstance method.
3. Give the SDK your ClientID and ClientSecret using the initialize method. Post initialization, SDK APIs can be used internally across the project.

<aside class="notice">
You must replace <code>ClientId</code> and <code>ClientSecret</code> with your credentials.
</aside>

# Resorts

## Get All Resorts

```kotlin
val venueService = VenueService.createInstance(applicationContext)

//Using Kotlin Coroutines
CoroutineScope(Dispatchers.IO).launch {      
    val venues = venueService.getVenues()
}

//Using Callback
venueService.getVenues(object: CompletionCallback<List<Venue>> {
    override fun onSuccess(result: List<Venue>) {
      //Get Venues Successful
    }

    override fun onError(throwable: Throwable) {
      //Get Venues Error
    }
})
```

```java 
VenueService venueService = VenueService.createInstance(getApplicationContext());
venueService.getVenues(new CompletionCallback<List<Venue>>() {
    @Override
    public void onSuccess(List<Venue> result) {
        //Get Venues Successful
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        //Get Venues Error
    }
});
```

This function provides a list of resorts associated with a particular Client Id. The data returned includes all details of available resorts.

## Get a Specific Resort

```kotlin
val venueService = VenueService.createInstance(applicationContext);
//Using Kotlin Coroutines
CoroutineScope(Dispatchers.IO).launch {
    venueService.getVenue(<ID>)
}

//Using Callback
venueService.getVenue(<ID>, object: CompletionCallback<Venue> {
    override fun onSuccess(result: Venue) {
        //Get Venue Successful
    }

    override fun onError(throwable: Throwable) {
        //Get Venue Error
    }
})
```

```java
VenueService venueService = VenueService.createInstance(getApplicationContext());
venueService.getVenue(<ID>, new CompletionCallback<Venue>() {
    @Override
    public void onSuccess(Venue venue) {
        //Get Venue Successful
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        //Get Venue Error
    }
});
```

This function helps to get the details for a specific resort using its identifier.

<aside class="warning">
  You can only get resorts that you are allowed to fetch.
</aside>

### Parameters

Parameter | DataType | Description
--------- | -------- | -----------
ResortId  | String   | The ID of the resort to retrieve


# Map

```kotlin
//Using XML UI (with View Binding or Data Binding)
val map = Map(venue)
map.load(this, object : LoadListener() {
    fun onSuccess(map: Map) {
        binding.mapView.setMap(map)
    }

    fun onError(throwable: Throwable) {
        // Error Handling
    }
})

//Using Jetpack Compose       
MapView(
    map = Map(venue),
    modifier = Modifier,
    mapController = MapController(),
    loadingView = { },
    errorView = { }
)

```

```java
Map map = new Map(venue);
map.load(this, new Map.LoadListener() {
    @Override
    public void onSuccess(@NonNull Map map) {
        binding.mapView.setMap(map);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        // Error Handling
    }
});
```

A map is an object that represents the 2D map. You need to inject all the required data associated with a resort to instantiate and load a Map.

# Scene

```kotlin
//Using XML UI (with View Binding or Data Binding)
val scene = Scene(venue)
scene.load(this, object : LoadListener() {
    fun onSuccess(scene: Scene) {
        binding.sceneView.setScene(scene)
    }

    fun onError(throwable: Throwable) {
        // Error Handling
    }
})

//Using Jetpack Compose       
SceneView(
    scene = Scene(venue),
    modifier = Modifier,
    sceneController = SceneController(),
    loadingView = { },
    errorView = { }
)

```

```java
Scene scene = new Scene(venue);
scene.load(this, new Scene.LoadListener() {
    @Override
    public void onSuccess(@NonNull Scene scene) {
        binding.sceneView.setScene(scene);
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        // Error Handling
    }
});
```

A scene is an object that represents the 3D map. You need to inject all the required data associated with a resort to instantiate and load a Scene.
