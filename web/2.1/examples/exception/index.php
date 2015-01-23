<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - Exception</TITLE>
</HEAD>
<BODY>
<?
include '../../google.php'
?>
<H2>Exception</H2>
<P>
Return 1 near some point in 2D and 0 everywhere else. This is slightly more complicated than XOR, but still ok for a basic backpropagation. This task illustrates well the problem of overfitting. The algorithm sometimes tends to use holes in the training set and catch the exception between 2 hyperplanes. This solution produces 0 error in training set but the generalisation is not right. Like in XOR, 2 layers are ok for this task.
</P>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/exception/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/exception/network.xml">Import the solution</A></LI>
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
