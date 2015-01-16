<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - Distance</TITLE>
</HEAD>
<BODY>
<?
include '../../google.php'
?>
<H2>Distance</H2>
<P>
Calculate a distance of the point in 2D from [0.5, 0.5]. Learn from the table and generalize to non training set examples. This task seems quite hard to learn, it requires some time and sometimes also several attemps. When trained, you will get results like distance(0.0, 0.5) = 0.47 so it works quite well incluiding the generalisation but it is clear that learning should be improved in this case. It is a TODO for me.
</P>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.0/examples/distance/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.0/examples/distance/network.xml">Import the solution</A></LI>
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
