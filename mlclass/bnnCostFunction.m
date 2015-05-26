function [J grad] = nnCostFunction(nn_params, input_layer_size, hidden_layer_size, num_labels, X, y, lambda)

Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), hidden_layer_size, (input_layer_size + 1));
Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), num_labels, (hidden_layer_size + 1));

m = size(X, 1);

J = 0;
Theta1GradientDescent = zeros(size(Theta1));
Theta2GradientDescent = zeros(size(Theta2));

a1 = [ones(m, 1) X];

knl = eye(num_labels);
y = knl(y,:);

z2 = a1 * Theta1';
a2 = sigmoid(z2);

a2 = [ones(m, 1) a2];
z3 = a2 * Theta2';
a3 = sigmoid(z3);

Theta1Filtered = Theta1(:, 2:end);
Theta2Filtered = Theta2(:, 2:end);
Jcost = (1/m) .* sum(sum( (-y).*log(a3) - (1-y).*log(1-a3) )) + (lambda/(2*m)).* (sum(sum(Theta1Filtered.^2)) + sum(sum(Theta2Filtered.^2)));

accuDelta1 = 0;
accuDelta2 = 0;


deltaErr3 = a3 - y;
z2 = [ones(m,1) z2];
deltaErr2 = deltaErr3 * Theta2 .* sigmoidGradient(z2);
deltaErr2 = deltaErr2(:, 2:end);

accuDelta1 = accuDelta1 + deltaErr2'*a1; 
accuDelta2 = accuDelta2 + deltaErr3'*a2; 

Theta1GradientDescent = (1/m).*accuDelta1;
Theta2GradientDescent = (1/m).*accuDelta2;

grad = [Theta1GradientDescent(:) ; Theta2GradientDescent(:)];

end