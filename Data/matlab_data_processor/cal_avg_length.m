function  avg_length =  cal_avg_length(cleaned_data) 
    data_size = size(cleaned_data,2);
    avg_length = 0;
    count = 0;
    for i = 1:data_size
        if(cleaned_data(i).label <= 8) 
            count = count + 1;
            avg_length= avg_length + size(cleaned_data(i).seq,2); 
        end
    end
    avg_length = avg_length/count;
end
