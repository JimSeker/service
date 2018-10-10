Android Service Examples
===========
eclipse/ has the examples in eclipse project format, no longer updated.  Otherwise the examples are for android studio.

<b>ServiceDemo</b> is a example of how to call Services, both IntentService and a Service.  plus an example of the download service too.

<b>ServiceDemoIPC</b> is an very simple example of how to use the binder pieces so an activity/fragment can call into the service.

<b>ServiceDemoMSG</b> is a simple example of passing a messages from the service to the activity via a handler.

<b>ForegroundServiceDemo</b> is an example of how to create a foreground service which can run without an app.  It needs a persistent notification
and to be started in the foreground.  This is required in 26/Oreo+, otherwise use a JobIntentService.

<b>FreeFallSrv</b> is an example of using a service to get sensor data and do things.  The activity only starts and stops the service.
The code based on the freefall code in the sensor repo.  Note with the background restrictions started in API 27, this app doesn't work as well as 
it used too.  Likely a JobService or a foreground service maybe the better way to go.

<b>JobIntentServiceDemo</b> is an example of a jobIntentService.  The jobIntentService just toasts a number of random numbers as asked for.
  this JobIntentServices can run in the background without an app in 26/Oreo+

<b>JobServiceDemo</b> is an example of a jobService.  The jobIntentService just toasts a number of random numbers as asked for.
Shows how to schedule a one off (with a recurring if you want) and a recurring one that survives reboot as well.

<b>JobServiceJobWorkItemDemo</b> is JobService, but instead of scheduling, it uses the new enqueue and dequeue methods.  
Note: This is a API 26/Oreo+ example.

<b>WorkManagerDemo</b> is an example of how to use the new Architecture Worker Tasks for background tasks.  This examples show a simple one, one with parameters,
and a set of tasks chained together.  The tasks don't actually do anything interesting.   This should work on API 19+ (but must be compiled with API 28).
When the worker library is moved the androidx, I'll change the example to use androidx support libraries. 

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course.
All examples are for Android.
