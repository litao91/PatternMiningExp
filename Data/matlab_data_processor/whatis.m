% a helper function that displays the lable and id
% of a character with a specific index.
function [char_id, the_char] =  whatis(Data,char_idx, is_print)
char_id = Data.consts.charlabels(char_idx);
the_char = Data.consts.key{char_id};
if(is_print)
fprintf('The %d seq has key %d, coresponding to %s \n', char_idx, char_id, the_char);
end
end

