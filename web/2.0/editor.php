<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Editor</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Network editor</H2>
<P>
Welcome to the neural network editor screen. This screen always opens the current network so good way to start is to load some predefined exaple and edit it here. If you make an error, siply go back to this editor and you can modify your network again.
</P>

<H3><A HREF="anatomy.php">Edit anatomy</A></H3>
<P>
Basic backpropagation cannot train deep networks. So it normaly succeed when there is just single hidden layer. The left most number is the input dimension, medium numbers are hidden layers neuron counts and the right most number is neuron count in the last layer which is also the outpout dimension. Press the button to edit it.
</P>

<H3><A HREF="trainingset.php">Edit training set</A></H3>
<P>
Training set is a set of examples with an input and expected output. For example if you want to teach your network prime numbers there will be for example (1)-&gt;(0), (2)-&gt;(1), (3)-&gt;(1), (4)-&gt;(0), (5)-&gt;(1), (6)-&gt;(0) and (8)-&gt;(0) and you will hope that after training, yor network will calculate not only this set well but it will also for example give for input 7 result 1. (And your network will fail because this is to complex problem :-))
</P>
<?
include 'links.php'
?>
</BODY>
</HTML>

