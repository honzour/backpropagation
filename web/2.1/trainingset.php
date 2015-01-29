<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Training set</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Training set</H2>
<P>
Training set is simply a set of pairs (input x, expected output for input x). Feel free to type any real numbers but input will be scaled to <I>&lt;-1, 1&gt;<SUP>m</SUP></I> (it is better for the algorithm) and output to <I>&lt;0, 1&gt;<SUP>m</SUP></I> (neuron output is always between 0 and 1). It will be rescaled during the computing back so you will always work with your original numbers.
</P>
<P>
Press item in the list to edit single training set element or to delete it. Press "Add an element" to add a new training set elemet.
</P>
<?
include 'links.php'
?>
</BODY>
</HTML>

