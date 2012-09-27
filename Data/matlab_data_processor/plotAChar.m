% plot the trojactory of a character
function plotAChar(Data, char_idx)
data = Data.mixout{char_idx};
x = data(1,:);
y = data(2,:);
plot(x,y, '-x');
whatis(Data, char_idx,1);
end
