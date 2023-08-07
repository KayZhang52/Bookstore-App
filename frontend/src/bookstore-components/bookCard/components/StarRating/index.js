/**
=========================================================

██╗░░██╗░█████╗░██╗░░░██╗
██║░██╔╝██╔══██╗╚██╗░██╔╝
█████═╝░███████║░╚████╔╝░
██╔═██╗░██╔══██║░░╚██╔╝░░
██║░╚██╗██║░░██║░░░██║░░░
╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░
 =========================================================

*/

import { useState } from "react";

// @mui material components
import { Rating } from "@mui/material";

import MDBox from "components/MDBox";


function StarRating({ value }) {
    // const [value, setValue] = useState(1);
    return (
        <MDBox
            sx={{
                "& > legend": { mt: 2 },
            }}
        >
            <Rating
                readOnly
                value={value}
            // onChange={(event, newValue) => {
            //     setValue(newValue);
            // }}
            />
        </MDBox>
    );
}

export default StarRating;