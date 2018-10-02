# Android Slices Example
> It is a project that implements a lot of useful examples to know more the new Android Slices that is introduced recently in Google I/O 2018.

![](https://i0.wp.com/storage.googleapis.com/gweb-uniblog-publish-prod/original_images/Slices_Blog.gif)

Slice is the UI component that can display the contents from your application from within the Google Search App and later in other places like the Google Assistant.

## Introduction :

Slices are Android’s new approach for remote content. There is a couple of important aspects to this which are :
 - **Templated :** which means that Slices are providing you with a rich layout and content system to enable a wide variety of app use cases. So, they are flexible in that you define a wide variety of elements and define them in a priority order, so to enable a rich variety of ways to express your app.
 - **Interactive :** They are not just a static data. They can house the variety of components, anything from real time data fetched directly from the APK on a user’s device - For example, surfacing the latest prices for ride sharing - as well as, rich controls, like actions, toggles, sliders, and scrolling content, so that the user can complete the task inline right where the slices is being presented.
 - **Updatable :** Slices are bundled with Jetpack. That means Slices can more frequently expand and iterate slices. That means Slices creators can add more presenting surfaces to expand your app’s reach, more templates to represent you app’s use cases, and more controls and components to make those slices more engaging and more powerful for users.
 - **Backward Compatible :** which means that all the way back to Android KitKat, which means that they are going to be available for 95% of Android devices.

This year, slices are launching in search, where Slices creators are going to use slices to enhance app predictions as the user is searching. There are going to be two main use cases in terms of how slices will appear.

## Android Slices Use-cases :

<img src="https://cdn-images-1.medium.com/max/1600/1*H2FFdhHrv_zMO0ERa8NAlg.png" height="400" />

 With slices, you are going to be able to enable a wide variety of really great use cases for people on search :
 - **The Navigation Use Cases :** user is searching for something that they already know they want to get to. For example the user knows that they already want to get a ride home. Well, with slices, you can use real time data to provide additional context for that decision so that they know the price and the time to their destination.
 - **The Task Completion Use Cases :** you might be able to use those interactive controls, so that apps like settings can enable use to quickly search for a setting and toggle inline.
 - **The Recall and Discovery Use Cases :** users might be searching for things that they didn’t even realize is available in your app, but because it’s installed on their device and that you can surface slices for it.

## Android Slice Architecture:

<img src="https://www.yudiz.com/wp-content/uploads/2018/07/slices1.jpg" height="400" />

What a slices actually is in terms of implementation, it is a structured data that contains other slices, content, and presentation hints. So, how does apps actually get a hold of slices from your app:
 - So, slices sit on top of content providers which means that SliceProvider extends ContentProvider.
 - But there are a few really nice things that come out of this is that they are based on top of content URIs, which means you can have wide variety of slices hosted from your app.
 - And when an app wants to show your slices, you will get a callback to slices and you will get a URI and you will get to decide what content you want to connect to URI and return it in this slice.
 - But then they are interactive, so you need to actually be able to update that. And the way that works is through a notified change. You send a standard content provider notify change on your slice URI.
 - And however presenting your slice will know that it’s time to update, and they will give you another callback. And you can return different type of data that is dynamic and changes in response to whatever has happened.

## Creating Slices:

You will find here a lot of examples for how creating different types of slices from basic to interactive to dynamic to delay content slices. Check the project, it is very sample to understand and well organized to navigate between classes and layers easily.

## Contributing

1. Fork it (<https://github.com/ahmed-adel-said/android-slices-example/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request.

## License

Copyright (c) 2018 Ahmed Adel, GithubRepos is released under the [this license](https://github.com/ahmed-adel-said/android-slices-example/blob/master/LICENSE).
