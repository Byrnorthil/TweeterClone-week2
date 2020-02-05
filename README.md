# Project 2 - *Name of App Here*

**Name of your app** is an android app that allows a user to view his Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **5.5** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweets from their home timeline**
  - [x] User is displayed the username, name, and body for each tweet
  - [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
- [x] User can refresh tweets timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweets as they scroll with infinite pagination
- [ ] User can tap a tweet to display a "detailed" view of that tweet
- [ ] User can open the twitter app offline and see last loaded tweets
- [ ] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.
- [ ] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.org/android/Drawables#vector-drawables) where appropriate.
- [ ] User can see embedded image media within the tweet detail view
- [ ] User sees an **indeterminate progress indicator** when any background or network task is happening
- [ ] User can **see embedded image media within a tweet** on list or detail view.
- [x] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
- [ ] User can view following / followers list through any profile they view.
- [ ] User is using **"Twitter branded" colors and styles**

The following **additional** features are implemented:

- [x] Used databinding to remove findViewById

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/Byrnorthil/TweeterClone/blob/master/20200203_225633.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

## Notes

I had trouble logging in because my Twitter account had been suspended for "impersonating" myself. The Twitter app frequently shut me out because of request overload, especially during infinite pagination implementation;

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

### Troubleshooting

* If you receive the following error `org.scribe.exceptions.OAuthException: Cannot send unauthenticated requests for TwitterApi client. Please attach an access token!` then check the following:
 * Is your intent-filter with `<data>` attached to the `LoginActivity`? If not, make sure that the `LoginActivity` receives the request after OAuth authorization.
 * Is the `onLoginSuccess` method being executed in the `LoginActivity`. On launch of your app, be sure to start the app on the LoginActivity so authentication routines execute on launch and take you to the authenticated activity.
 * If you are plan to test with Android API 24 or above, you will need to use Chrome to launch the OAuth flow.  
 * Note that the emulators (both the Google-provided x86 and Genymotion versions) for API 24+ versions can introduce intermittent issues when initiating the OAuth flow for the first time.  For best results, use an device for this project.
