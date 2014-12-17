<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - New task</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>New task</H2>
<P>
You can open a network here by two ways or also edit the current network.
</P>
<H3>Read from www</H3>
<P>
This is the best way for beginners with Internet connection. This program has a <A HREF="examples/">set of examples</A> on the Internet with a picture, a basic description and a special backpropagation:// URL, so you can import it into the application by a single tap from the browser.

<H3>Internal example</H3>
<P>
The most simple way but without description. Let say you want to open the popular XOR problem. Simply change the spinner value from 'Identity' to 'XOR' and press 'Load example' and it is all. Of course you can choose just several hardcoded examples here but this is the simplest way to start. Please note that you are loading just the network anatomy (input dimension, number of layers and number of neurons in each layers) and training set. This means you have to teach the network its training set when it is loaded.
</P>

<H3><A HREF="editor.php">Editor</A></H3>
<P>
If you are not satisfied with predefined examples, this will be the simplest way for you how to define you own network. Editor always opens the current network so good way to start is to load some example and modify it here. Press 'Run editor' to start it in the new window. Again, you are editing just the anatomy and training set, not the learning status (weight of each connection).
</P>
<?
include 'links.php'
?>
</BODY>
</HTML>

