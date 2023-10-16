Android Service Examples
===========
`eclipse/` has the examples in eclipse project format, no longer updated.  Otherwise the examples are for android studio.

`ForegroundServiceDemo` is an example of how to create a foreground service which can run without an app.  It needs a persistent notification
and to be started in the foreground.  This is required in 26/Oreo+, otherwise use a JobIntentService.

`FreeFallSrv` is an example of using a service to get sensor data and do things.  The activity only starts and stops the service.
The code based on the freefall code in the sensor repo.  Note with the background restrictions started in API 27, this app doesn't work as well as 
it used too, you need to leave the app running for it to work.  Likely a JobService or a foreground service maybe the better way to go.

`JobIntentServiceDemo` is an example of a jobIntentService.  The jobIntentService just toasts a number of random numbers as asked for.
  this JobIntentServices can run in the background without an app in 26/Oreo+

`JobServiceDemo` is an example of a jobService.  The jobIntentService just toasts a number of random numbers as asked for.
Shows how to schedule a one off (with a recurring if you want) and a recurring one that survives reboot as well.

`JobServiceJobWorkItemDemo` is JobService, but instead of scheduling, it uses the new enqueue and dequeue methods.  Note: This is a API 26/Oreo+ example.

`ServiceDemo` is a example of how to call Services, both IntentService and a Service.  for the DownloadService see [DownloadDemo](https://github.com/JimSeker/networking)

`ServiceDemoIPC` is an very simple example of how to use the binder pieces so an activity/fragment can call into the service.

`ServiceDemoMSG` is a simple example of passing a messages from the service to the activity via a handler.

`WorkManagerDemo` is an example of how to use the new Architecture Worker Tasks for background tasks.  This examples show a simple one, one with parameters, and a set of tasks chained together.  The tasks don't actually do anything interesting.   

---

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.

