<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Main screen</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Main screen</H2>
<P>
Welcome to the main screen of Android application called Backpropagation. If you have just started the program for the first time, there is no network loaded yet, so the only thing you can do now is to read this help page or add a new task. Or read <A HREF="index.php">several sentences</A> about this program first.
<H3><A HREF="newtask.html">New task</A></H3>
<P>
This is exactly you should start with. There are several ways how to create or import the new task or edit an existing task. Do not worry if you are a beginner, import from several hardcoded tasks is also possible.
</P>
<H3><A HREF="learning.php">Learning</A></H3>
<P>
If you have created or loaded a new task, all weights ar set to a small random number (the network will fail even in training set examples) so you have to teach it first. So this is probably the first thing you should do with the new task.
</P>
<H3>Network visualisation</H3>
<P>
See the network anatomy - all the layers and neurons, each connection and weight. Please note that the picture is on a single mobile device screen so this is useful for small networks only, say XOR problem or so.
</P>
<H3>Result visualisation</H3>
<P>
This will show you the thaining set and all pixels near training set in 2D -> 3D graphics. Input 2D is x and y coordinate in the screen and output 3D is RGB of the color. It shows pretty well the curront status of the learning if input dimension is less or equal than 2 and output dimension is less or equal than 3. It is not much usefull for higher dimension.
</P>
<H3>Result for input</H3>
<P>
Give some input (exact numbers) to the network and it will calculate the output (exact numbers). For example for XOR problem you can set (1, 0) and you will get probably something like (0.999).
</P>
<H3>Export to xml</H3>
<P>
You can save the current task (including training set nad all neuron weights) to a XML file to you SD card or send it by e-mail to yourself. Press 'New task' if you want to restore it in this program again.
</P>
<H3>Help</H3>
<P>
The button simply opens the system default www browser with this help screen. Please note that you can press MENU-&gt;Help at any place of this program and you will get the context help for each screen.
</P>
</P>
</BODY>
</HTML>

