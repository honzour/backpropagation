<?
include '../../util.php';
example("Exception", "Return 1 near some point in 2D and 0 everywhere else. This is slightly more complicated than XOR, but still ok for a basic backpropagation. This task illustrates well the problem of overfitting. The algorithm sometimes tends to use holes in the training set and catch the exception between 2 hyperplanes. This solution produces 0 error in training set but the generalisation is not right. Like in XOR, 2 layers are ok for this task.");
?>

