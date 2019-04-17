# Task: “Create a Mini Mercari App”

### Highlights: 
- Total time spent* ~ 11 hours

- App Architecture: MVVM based on AAC LiveData and ViewModel.

- DI framework: Koin. I like it more than Dagger because it's easier to understand and faster to implement.

- Networking: Retrofit+RxJava. Nothing special :) I like coroutines more but most people still prefer RxJava.

- Caching: Room. I wanted to cache products data in order to not reload it every time when the user switches the tab. The cache is valid while the app is alive. On every app start, I clear it. You can still force to refresh products list using "Swipe To Refresh".

- I also implemented an error state if there is no data to show and snackbar if we already have data in the cache. The error state is a compound view. 

### Possible questions and answers: 
- Q: Why the design looks different?
- A: I changed it a bit to make it closer to guidelines. I changed only minor things that don't effect on the task itself. 


- Q: Why there are no get methods returning LiveData instead of MutableLiveData as Google suggest?
- A: Actually there are 2 reasons: 
  * I see no reaso why somebody would try to change it from the view without having a bottle of whiskey before
  * On big projects, there are usually many livedata fields and these methods just make ViewModel less readable and too long IMHO.


- Q: android:foreground doesn't work for ConstraintLayout API<23, why did you add it to item_product.xml ?
- A: It's not major but I just wanted to see it on the pressed state. Can wrap everything with FrameLayout to see the same effect :) 
