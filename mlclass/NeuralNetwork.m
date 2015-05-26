clear ; close all; clc

inputLayerSize  = 400; 
hiddenLayerSize = 25;
kLabels = 10;           

load('ex4data1.mat');
m = size(X, 1);

% randomly selects 100 data points to display
sel = randperm(size(X, 1));
sel = sel(1:100);

displayData(X(sel, :));

fprintf('Initializing Neural Network Parameters ...\n')

initTheta1 = randInitializeWeights(inputLayerSize, hiddenLayerSize);
initTheta2 = randInitializeWeights(hiddenLayerSize, kLabels);

initial_nn_params = [initTheta1(:) ; initTheta2(:)];

fprintf('Program paused. Press enter to continue.\n\n');
pause;

fprintf('Training Neural Network ...\n')

lambda = 1;
costFunction = @(p) nnCostFunction(p, inputLayerSize, hiddenLayerSize, kLabels, X, y, lambda);
options = optimset('MaxIter', 200);
[nn_params, cost] = fmincg(costFunction, initial_nn_params, options);

Theta1 = reshape(nn_params(1:hiddenLayerSize * (inputLayerSize + 1)), hiddenLayerSize, (inputLayerSize + 1));

Theta2 = reshape(nn_params((1 + (hiddenLayerSize * (inputLayerSize + 1))):end), kLabels, (hiddenLayerSize + 1));

fprintf('Program paused. Press enter to continue.\n');
pause;

fprintf('\nVisualizing Hidden Layer of Neural Network... \n')

displayData(Theta1(:, 2:end));

fprintf('\nProgram paused. Press enter to continue.\n');
pause;

pred = predict(Theta1, Theta2, X);

fprintf('\nTraining Set Accuracy: %f\n', mean(double(pred == y)) * 100);

rp = randperm(m);

for i = 1:m
  fprintf('\nDisplaying Example Image\n');
  displayData(X(rp(i), :));
  pred = predict(Theta1, Theta2, X(rp(i),:));
  fprintf('\nNeural Network Prediction: %d (digit %d)\n', pred, mod(pred, 10));
  fprintf('Program paused. Press enter to continue.\n');
  pause;
end
