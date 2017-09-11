Android Service Examples
===========
eclipse/ has the examples in eclipse project format, no longer updated.  Otherwise the examples are for android studio.

<b>ServiceDemo</b> is a example of how to call Services, both IntentService and a Service.  plus an example of the download service too.

<b>ServiceDemoIPC</b> is an very simple example of how to use the binder pieces so an activity/fragment can call into the service.

<b>ServiceDemoMSG</b> is a simple example of passing a messages from the service to the activity via a handler.

<b>FreeFallSrv</b> is an example of using a service to get sensor data and do things.  The activity only starts and stops the service.
The code based on the freefall code in the sensor repo.

<b>JobIntentServiceDemo</b> is an example of a jobIntentService.  The jobIntentService just toasts a number of random numbers as asked for.
  this JobIntentServices can run in the background without an app in Oreo+

<b>JobServiceDemo</b> is an example of a jobService.  The jobIntentService just toasts a number of random numbers as asked for.
  
<b>ForegroundServiceDemo</b> is an example of how to create a foreground service which can run without an app.  It needs a presistent notification
and to be started in the foreground.  This required in Oreo+, otherwise use a JobIntentService.
  
  
These are example code for University of Wyoming, Cosc 4730 Mobile Programming course.
All examples are for Android.
