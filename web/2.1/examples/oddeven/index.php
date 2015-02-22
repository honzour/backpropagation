<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - Odd - Even</TITLE>
</HEAD>
<BODY>
<?
include '../../google.php'
?>
<H2>Odd - Even</H2>
<P>
Returns 1 for odd and 0 for even numbers. The input is simply 1D, the real value of the number scaled to &lt;0, 1&gt;. Training set is from 0 to 6 and the network can learn the training set pretty well. It fails for larger training set and it also fails to generalize for numbers that are not in the training set. Anyway, I would like to know how a common neural network with sigma activation fuction can generalize here...
</P>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/oddeven/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/oddeven/network.xml">Import the solution</A></LI>
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
