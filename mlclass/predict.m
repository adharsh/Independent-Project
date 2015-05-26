function p = predict(Theta1, Theta2, X)

m = size(X, 1);
num_labels = size(Theta2, 1);

p = zeros(size(X, 1), 1);

a1 = [ones(m,1) X];
a1 = a1';

z2 = Theta1 * a1;
a2 = sigmoid(z2);

a2 = [ones(1,m); a2];

z3 = Theta2 * a2;
a3 = sigmoid(z3);

[prediction, p] = max(a3', [], 2); 

end