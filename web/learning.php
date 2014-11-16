<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Learning</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Learning</H2>
<P>
When you load an example or create your own task in the editor, it will have all connections and tresholds weights set to some small random number near zero. This means that the network is not trained and it will fail even on the training set examples. Learning means adapting all weights to get the minimal medium quadratic error on training set exaples. If we succseed, error on training set will be very small and we will hope that it is because the network got some hidden rules from training set data so it will get good result for each inout, not just training set elements. Adapting  is beeing done by the backpropagation algorithm. It works in steps and we calculate derivation of training set error per each weight and move the weights vector to the right direction. Algorithm goes from the last layer to the first so it is called backpropagation. There is a learning speed parameter called 'alpha'. If you set it to a big number, the algorithm will move the weights in each step by a big number so it will go much faster in clear and simple cases, but it can divergate in complex tasks. You can stop learning by 'Stop' button or leaving this screen by a back key when the error is small enough.
</P>
<P>
If you want to know more about this algorithm, I will recommend you <A HREF="https://www.coursera.org/course/neuralnets">this free coursera course</A>.
</P>
<P>
If the learning is not successful you can try
</P>
<H3>Restart learning</H3>
<P>
This will reset all the weights to the random number and you can start from scratch. This is useful when the algorithm was stopped in some local minimum.
</P>

<H3>Restart neuron</H3>
<P>
The same but just one neuron is reset.
</P>
<P>
Still not succseeded? Check your anatomy. Are there enough neurons for this task? Are there not to many layer? Note that two hidden layers are normaly to much for a basic backpropagation. And one hidden layer is not enough for any complex task :-) I promiss I will try to improve the algorithm in the future.
</P>
</BODY>
</HTML>

