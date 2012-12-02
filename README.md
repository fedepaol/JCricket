JCricket
=========

JCricket is a simple android library to show popup hints over views.

You can define a popup using any layout you want, and edit its content programmatically.

Usage
-------

The usage is quite simple and relies on builder pattern.

```java
PopupHint.PopupBuilder b = new PopupHint.PopupBuilder(this);
mHint = b.layout(R.layout.baloon).location(PopupLocation.TOP_LEFT, PopupCorner.BOTTOM_RIGHT).build();
mHint.showHint(targetView);
```

The location of the popup is expressed in terms of position related to the targetview:

----


![alt text](https://raw.github.com/fedepaol/JCricket/master/pics/Baloon_position.png "Logo Title Text 21")


----

On top of that, you can choose which corner of the popup to place in the given position (the blue one is the popup)

----

![alt text](https://raw.github.com/fedepaol/JCricket/master/pics/Baloon_reference.png "Logo Title Text 1")

---

The red dot means that the choosen popup location was top left. The two popups where are respectively bottom left and bottom right. The popup corner argument means which corner of the popup you want to place in the choosen position of the target view.

Another example:

----

![alt text](https://raw.github.com/fedepaol/JCricket/master/pics/sample_reference.png "Logo Title Text 1")

----

Other features:
-----------

* you can set a dismiss listener
* the popup is dismissed automatically whenever is pressed
* you can access to the subviews of the popup and change them at runtime using _getHintSubview_

    


===================


Copyright 2012 Federico Paolinelli Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
