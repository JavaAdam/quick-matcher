Quick Matcher supports the matching of projects in Package Explorer and SVN log entries in the History (Subversive).

# Installation #
To install Quick Matcher in Eclipse use the following update site: http://quick-matcher.googlecode.com/git/quick-matcher/updatesite/

The minimal required Eclipse version is Ganymede (3.4.0) and Java Runtime 1.6.

**Attention:** Quick Matcher does not work in Eclipse 4 (Juno) because of this [Bug 38379](https://bugs.eclipse.org/bugs/show_bug.cgi?id=383679).

# General #
The typed filter text matches the target information by a contains comparison. The  `*`-placeholder can be used as a wildcard for any characters. Further the OR-Operator ("|") can be used to aggregate several matching statements.

# Project Matcher #

## Description ##
Project Matcher provides a text field in the Package Explorer's toolbar for typing a substring of a focused project name. All other projects which does **not** contain this substring in their name are filtered. Thus, all unimportant projects are instantly made invisible.

![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/no_filtering.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/no_filtering.png) ![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filtering.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filtering.png)

## Configuration ##
The filter mechanism of Project Matcher is a viewer filter, which can be turned on and off by the view's "Filters..." option.

![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filters1.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filters1.png)

![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filters2.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/filters2.png)

# Subversive History Matcher #
## Description ##
Subversive History Matcher provides a text field in the History's toolbar for typing a substring of a focused log entry. All other entries which does **not** contain this substring in their Revision, Author or Comment field are filtered. Thus, all unimportant log entries are instantly made invisible.

![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/history_1.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/history_1.png)

![http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/history_2.png](http://quick-matcher.googlecode.com/git/quick-matcher/documentation/wiki/images/history_2.png)

# Release Notes #
## Version 1.2 ##
  * Minor API Extension: Text of ToolTip and InputBox is setable.
## Version 1.1.1 ##
  * Commons Plug-In can be used for RAP now
## Version 1.1 ##
  * OR-Operator (|) enabled

# License #
Copyright (c) 2012 Adam Wehner. All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html

# Acknowledgement #
  * Markus Krüger: for technical hints, creative pulses and contributions
  * Benjamin Weyers: for linguistic help
