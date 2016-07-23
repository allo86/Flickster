# Week 1 Project: Flicks

**Flickster** shows the latest movies currently playing in theaters. The app utilizes the Movie Database API to display images and basic information about these movies to the user.

Time spent: **12** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **scroll through current movies** from the Movie Database API
* [x] Layout is optimized with the usage of RecyclerView and RecyclerView.ViewHolder
* [x] For each movie displayed, user can see the following details:
  * [x] Title, Poster Image, Overview (Portrait mode)
  * [x] Title, Backdrop Image, Overview (Landscape mode)

The following **optional** features are implemented:

* [x] User can **pull-to-refresh** popular stream to get the latest movies.
* [x] Display a nice default [placeholder graphic](http://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library#configuring-picasso) for each image during loading (I used a circular progress bar)
* [x] Improved the user interface through styling and coloring.

The following **bonus** features are implemented:

* [x] Allow user to view details of the movie including ratings and popularity within a separate activity or dialog fragment.
* [x] When viewing a popular movie (i.e. a movie voted for more than 5 stars) the video should show the full backdrop image as the layout.  Uses [Heterogenous ListViews](http://guides.codepath.com/android/Implementing-a-Heterogenous-ListView) or [Heterogenous RecyclerView](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) to show different layouts.
* [x] Allow video trailers to be played in full-screen using the YouTubePlayerView.
    * [x] Overlay a play icon for videos that can be played.
    * [x] More popular movies should start a separate activity that plays the video immediately.
    * [x] Less popular videos rely on the detail page should show ratings and a YouTube preview.
* [x] Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce boilerplate code.
* [x] Apply rounded corners for the poster or background images using [Picasso transformations](https://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library#other-transformations)

The following **additional** features are implemented:

* [x] Load list of videos in YouTubePlayerView
* [x] Share movie link - https://www.themoviedb.org/movie/id

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/A3vqUqI.gif' title='Video Walkthrough Portrait' width='' alt='Video Walkthrough Portrait' />

<img src='http://i.imgur.com/7gUE77e.gif' title='Video Walkthrough Portrait' width='' alt='Video Walkthrough Portrait' />

<img src='http://i.imgur.com/hdS9zm7.gif' title='Video Walkthrough YouTube' width='' alt='Video Walkthrough YouTube' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

- There is an strange problem using the YouTubePlayerSupportFragment. If the movie is shown in fullscreen, after the video is dismissed, the activity remains in landscape mode and, even if i rotate the device, the landscape mode still remains. It was necessary to force portrait mode righ after the video exists fullscreen mode. Also, in other emulator, after the fullscreen mode is dismissed, the container activity was completely restarted (onCreate was called)
- The RatingBar color is set up in code. The xml properties did not had the desired effect in some Android versions.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Picasso Transformations](https://github.com/wasabeef/picasso-transformations) - An Android transformation library providing a variety of image transformations for Picasso
- [Butterknife](http://jakewharton.github.io/butterknife/) - Bind Android views and callbacks to fields and methods
- [Icepick](https://github.com/frankiesardo/icepick) - Android Instance State made easy
- [ACProgressLite](https://github.com/Cloudist/ACProgressLite) - Android loading or progress dialog widget library, provide efficient way to implement iOS like loading dialog and progress wheel

## License

    Copyright [2016] [Andrés Llerena]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
