<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples</TITLE>
</HEAD>
<BODY>
<?
include '../google.php'
?>
<H3>Hello world examples</H3>
<UL>
<LI>
<B><A HREF="identity">Identity</A></B><BR>
<P>
A very basic hello world example. We simply want the network to return 0 for input 0 and 1 for input 1. Single layer network with just one neuron is used and even this is quite overkill for this task.
</P>
</LI>
<LI>
<B><A HREF="not">NOT</A></B><BR>
<P>
Like previous one, but we switch output values. So it should return 0 for input 1 and 1 for input 0. Single layer network with just one neuron is used and even this is quite overkill for this task.
</P>
</LI>
<LI>
<B><A HREF="and">AND</A></B><BR>
<P>
Compute the logical AND. Unlike previous examples this is 2D to 1D function. Single layer network with just one neuron with two inputs is used.
</P>
<B><A HREF="or">OR</A></B><BR>
<P>
Compute the logical OR. This is very similar to previous example and again single layer network with just one neuron with two inputs is used.
</P>
</LI>
</UL>

<H3>Simple examples</H3>
<UL>
<LI>
<B><A HREF="xor">XOR</A></B><BR>
<P>
Compute the logical XOR. Returns 1 if and only if exactly 1 of two input is 1. A pretty basic example what single layer network cannot do because the classification cannot be done by a single hyperplane (line in our 2D case). This is a very popular problem which caused lot of troubles to neural network research in the past, but it has been solved by the basic backpropagation algorithm very well. We simply use 2 layers. 2 neurons in the first layer make two hyperplanes and the single neuron in the last layer computes AND.
</P>
</LI>
<LI>
<B><A HREF="exception">Exception</A></B><BR>
<P>
Return 1 near some point in 2D and 0 everywhere else. This is slightly more complicated than XOR, but still ok for a basic backpropagation. This task illustrates well the problem of overfitting. The algorithm sometimes tends to use holes in the training set and catch the exception between 2 hyperplanes. This solution produces 0 error in training set but the generalisation is not right. Like in XOR, 2 layers are ok for this task.
</P>
</LI>
<LI>
<B><A HREF="chess">Chessboard 2x2</A></B><BR>
<P>
We have a chess board 2x2 and we want to return 1 on any pixel of the white field and 0 on any pixel of the black field. Looks simple but basic backpropagation seems to be useless for this task. It cannot be done by 1 layer network but also 2 layers is not enough. We must place hyperplanes between the fields (1 layer) and it reduces the problem to XOR (2 more layers). So we need 3 layers to compute this. And it is even worse, there is a pretty deep local minimum of the error function when we place hyperplanes in the first layer like in the basic XOR... Simple problem but hard for the backpropagation algorithm. The current version fails here but it is number one on my TODO list.
</P>
</LI>
<LI>
<B><A HREF="3d">Output 3D</A></B><BR>
<P>
All previous task had just 1 dimensional 0 or 1 output. This is a basic task with just several hyperplanes but the output is 3D and it can draw a colorful picture. Just a demonstration that the output can have higher dimension, nothing more.
</P>
</LI>
</UL>
<H3>Mathematical examples</H3>
<UL>
<LI>
<B><A HREF="plus">Plus</A></B><BR>
<P>
Simply return x + y. Learn from the table and generalize to non training set examples. When trained, you will be able to calculate for example 0.2 + 0.2 and you will get result like 0.39.
</P>
</LI>
<LI>
<B><A HREF="distance">Distance</A></B><BR>
<P>
Calculate a distance of the point in 2D from [0.5, 0.5]. Learn from the table and generalize to non training set examples. This task seems quite hard to learn, it requires some time and sometimes also several attemps. When trained, you will get results like distance(0.0, 0.5) = 0.47 so it works quite well incluiding the generalisation but it is clear that learning should be improved in this case. It is a TODO for me.
</P>
</LI>
<LI>
<B><A HREF="sinus">Sinus</A></B><BR>
<P>
0 to 10 pi of a simple sinus wave. There are no training set examples between 4 pi and 6 pi, so the medium sinus period in the middle is not covered by training set and the task for the network is to generalize. I expected fail of the generalisation here but the program even fails to learn the training set by any way (simply to get the training set error small). What more: each step of the backpropagation takes quite a long time and the other problem is that there are quite many neurons and training set examples and the program does not handle it nice in the GUI. So this sinus task is a total fail of the whole program but also a big TODO for me.
</P>
</LI>

</UL>

<?
include '../links.php'
?>
</BODY>
</HTML>

