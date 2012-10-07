function out4svm(cleaned_data, outfile) 
data_size = size(cleaned_data,2);
fp = fopen(outfile, 'w');
for i = 1:data_size
    count = 1;
    if(cleaned_data(i).label <= 8)   % Reduce the label to 8
        fprintf(fp,'%d ', cleaned_data(i).label);
        seq_size = size(cleaned_data(i).seq,2);
        for j = 1:seq_size
            fprintf(fp, '%d ', cleaned_data(i).seq(j));
        end
        fprintf(fp,'\n');
    end
end
fclose(fp);
end
