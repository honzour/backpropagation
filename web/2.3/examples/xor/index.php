<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - XOR</TITLE>
</HEAD>
<BODY>
<?
include '../../google.php'
?>
<H2>XOR</H2>
<P>
Compute the logical XOR. Returns 1 if and only if exactly 1 of two input is 1. A pretty basic example what single layer network cannot do because the classification cannot be done by a single hyperplane (line in our 2D case). This is a very popular problem which caused lot of troubles to neural network research in the past, but it has been solved by the basic backpropagation algorithm very well. We simply use 2 layers. 2 neurons in the first layer make two hyperplanes and the single neuron in the last layer computes AND.
</P>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/xor/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/xor/network.xml">Import the solution</A></LI>
<LI><A HREF="network.csv">View the problem CSV file</A></LI>
<LI><A HREF="network.xml">View the solution XML file</A></LI>
<LI><BR><IMG SRC="result.png">
<BR>Result visualisation</LI>
<LI><BR><IMG SRC="network.png">
<BR>Network visualisation</LI>
</UL>
<?
include '../../links.php'
?>
</BODY>
</HTML>
