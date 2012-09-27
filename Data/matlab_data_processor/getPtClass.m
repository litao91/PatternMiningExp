% classify the point according to it's position, the 
% space is divided into a 5 x 5 grid, and each point 
% is assigned to one of the 25 cells.
function ptClass = getPtClass(pt_x, pt_y)
if pt_x <-1.2
    pt_x = -1.199;
end

if pt_x > 1.2
    pt_x = 1.2;
end

if pt_y < -1.2
    pt_y = -1.199;
end

if pt_y > 1.2
    pt_y = 1.2;
end

% get the id for the x coordinate
iter = 1;
for seq = -1.2:0.48:1.2
    if pt_x > seq && pt_x <= seq + 0.48
        x_id = iter;
        break;
    end
    iter = iter + 1;
end

% get the id for the y coordinate:
iter = 1;
for seq = -1.2:0.48:1.2
    if pt_y > seq && pt_y <= seq + 0.48
        y_id = iter;
        break;
    end
    iter = iter + 1;
end

ptClass = (y_id-1)*5 + x_id;
end



