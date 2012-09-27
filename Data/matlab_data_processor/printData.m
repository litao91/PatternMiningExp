% display the data
function printData(Data, char_num, from, to) 
data = Data.mixout{char_num};
for i = from:to
    fprintf('%f %f %f\n', data(1,i), data(2, i), data(3,i));
end
end



