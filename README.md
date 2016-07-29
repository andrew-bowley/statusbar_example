# Example Application for Statusbar Eclipse E4 Plugin

## Introduction

![Statusbar Example App](/images/statusbar-example.png)

To find out more about the Statusbar Eclipse E4 Plugin, install and run this Eclipse E4 application, which contains a status line
with 3 - 4 items and controls to send events to the items so their behaviour can be observed.

## Statusbar

Statusbar is a fragment plugin which adds a status line to a pure E4 application.
Using the Eclipse 4 Model Editor, simply add to the main window a "Window Trim - Bottom" with ID "au.com.cybersearch2.statusline.trimbar".
Here is an example:


![Eclipse 4 Model Editor](/images/model-editor.png)

 
For information on Statusbar, go to our [support site] (http://cybersearch2.com.au/eclipse).

This plugin is a spin off from the development of the Cybertete Rich Client Platform (RCP) Instant Messaging Client
which is a research project into how to utilize the latest E4 technologies. Statusbar originated as
an integral part of Cybertete, but moving the status line implmentation to to a separate plugin seemed a
worthy project for code reuse. Hence Statusbar was created. The original design has been enhanced to increase flexibility 
and should be readily dropped into any E4 RCP application. 
The plugin is available online in P2 repository [https://dl.bintray.com/andrew-bowley/generic/Statusbar/V1.1.0] (https://dl.bintray.com/andrew-bowley/generic/Statusbar/V1.1.0). 

Available from the same P2 repository is the companion Control Factory plugin, a Statusbar dependency which facilitates unit testing
where code uses the Standard Widget Toolkit (SWT) - the Eclipse portable Graphics library. 
 