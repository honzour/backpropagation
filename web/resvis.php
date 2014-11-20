<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Result visualisation</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Result visualisation</H2>
<P>
This screen shows network result for all the pixels near training set. If we mark input <B>x</B> and output <B>y</B>, <I>x</I> coordinate will be <I>x[0]</I>, <I>y</I> coordinate <I>x[1]</I> and all other <I>x[i]</I> will be set to <I>0</I>. We calculate network result for each such input in each pixel of the image and the result will be shown as RGB calue of the pixel. <I>R = y[0]</I>, <I>G = y[1]</I> or <I>0</I> if output dimension is just one <I>B = y[2]</I> or <I>0</I> if output dimension is less than three. Circles are exaples from the training set. This visualisation is useful if your network calculate from 2D or less to 3D or less.
</P>
<?
include 'links.php'
?>
</BODY>
</HTML>

