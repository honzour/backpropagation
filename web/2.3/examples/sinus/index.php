<?
include '../../util.php';
exampleBool("Sinus", "0 to 10 pi of a simple sinus wave. There are no training set examples between 4 pi and 6 pi, so the medium sinus period in the middle is not covered by training set and the task for the network is to generalize. I expected fail of the generalisation here but the program even fails to learn the training set by any way (simply to get the training set error small). What more: each step of the backpropagation takes quite a long time and the other problem is that there are quite many neurons and training set examples and the program does not handle it nice in the GUI. So this sinus task is a total fail of the whole program but also a big TODO for me.", false);
?>

