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
Training set is simply a set of pairs (input x, expected output for input x). The current version of algorithm in this program does not work well if the numers from input are to large. If you want to get better result, please scale input to <I>&lt;-1, 1&gt;<SUP>n</SUP></I> where <I>n</I> is your input dimension. There is no multiplying layer at the end of the network so the output is always from <I>&lt;0, 1&gt;<SUP>m</SUP></I>. So output must be set by chackboxed where checked means 1 and unchecked 0. I have fixing of both that issues on my TODO list.
</P>

</BODY>
</HTML>

