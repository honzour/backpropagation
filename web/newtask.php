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
You can open or create a new network here by several ways or also edit existing network.
</P>
<H3>Predefined example</H3>
<P>
This is the most simple way for beginners. Let say you want to open the popular XOR problem. Simply change the spinner value from 'Identity' to 'XOR' and press 'Load example' and it is all. Of course you can choose just several hardcoded examples here but this is the simplest way to start. Please note that you are loading just the network anatomy (input dimension, number of layers and number of neurons in each layers) and training set. This mean you have to teach the network its training set when it is loaded.
</P>

<H3><A HREF="editor.php">Editor</A></H3>
<P>
If you are not satisfied with predefined examples, this will be the simplest way for you how to define you own network. Editor always opens the current network so good way to start is to load some example and modify it here. Press 'Run editor' to start it in the new window. Again, you are editing just the anatomy and training set, not the learning status (weight of each connection).
</P>

<H3>Load xml</H3>
<P>
Another way to get a new network into the program is to load it from a xml file. Unlike previous cases, there is not only the anatomy and training set but also all weights specified. So if you teach your network, save it into a xml and reload it again, it will stay trained. No more teaching will be needed for the second time. The file format is well human readable so if you are familiar with both xml and neural networks and you export to xml and read some examples, you will be able to create your own tasks.
</P>

</BODY>
</HTML>

