[![License](https://img.shields.io/:license-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)


Jessie
========

Creates a Maven skeleton application based on the options you specify.


Motivation
======

I need to create a lot of Maven test projects for trying out the features for the different Atbash projects.
I do this by copy and pasting a few files from other projects but I imagined it could be done easier and better by generating it.

Why not Maven artefacts?  
You don't have the possibility to specify the options you want. And creating an archetype for all possible combinations would become impossible.

Why not existing alternatives like _generjee_ or _factorEE_?  
They are no longer active or have the wrong focus (code generation). And what about features that I need like support for the Atbash projects.   

Why not JBoss Forge?  
Well that one was considered as a valid option. You can write your own plugins which would solve my requirements. But then you are using a large platform for just having the Command Line and template features.

So in the end, I decided to write my own version and practice my skills around templating, CDI 2 (Java SE), Java FX, etc .. 


Release Notes
====

This is a 0.1 version just to try out the different concepts.
