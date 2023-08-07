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

// react-router components
import { Link } from "react-router-dom";

// prop-types is a library for typechecking of props
import PropTypes from "prop-types";

// @mui material components
import Card from "@mui/material/Card";
import MuiLink from "@mui/material/Link";
import { Grid } from "@mui/material";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";
import StarRating from "./components/StarRating";
import { useEffect, useState } from "react";
import bamazonService from "services/bamazon-service";


function BookCard({ bookId, title, author, imgLocation, rating, price, reviewCount }) {
  const [coverImg, setCoverImg] = useState()

  const fetchImage = async (bookId) => {
    try {
      const res = await bamazonService.getImage(bookId);
      // console.log(typeof res);
      // console.log(typeof res.data);
      const url = URL.createObjectURL(res.data);
      setCoverImg(url)
    } catch (err) {
      // console.log("error: fetch image : ", err)
    }
  };

  useEffect(() => {
    fetchImage(bookId)
  }, [])

  return (
    <Card sx={{ maxWidth: "800px" }}>
      <MDBox p={3} >
        <Grid container spacing={3}>
          <Grid item xs={6} display="flex" justifyContent="center" alignItems="center">
            <MDBox
              component="img"
              src={coverImg}
              alt={title}
              borderRadius="lg"
              shadow="md"
              height="200px"
              // position="relative"
              zIndex={1}
            />
          </Grid>
          <Grid item xs >
            <MDBox>
              <MDBox mt={2} mb={3}>
                <MDTypography variant="h3" textTransform="capitalize" fontWeight="bold">
                  {title}
                </MDTypography>
                <MDTypography variant="subtitle" textTransform="capitalize">
                  {author}
                </MDTypography>
              </MDBox>
              <MDBox display="flex" >
                <MDBox >
                  <StarRating value={rating} />
                </MDBox>
                <MDBox ml={1}>
                  <MDTypography variant="body2" component="p" color="text">
                    {reviewCount} reviews
                  </MDTypography>
                </MDBox>

              </MDBox>
              <MDBox mt={2} mb={3}>
                <MDTypography variant="h3" component="p" color="text">
                  S${price}
                </MDTypography>
              </MDBox>
            </MDBox>

          </Grid>
        </Grid>
      </MDBox>
      <MDBox position="relative" borderRadius="lg" mt={-3} mx={2}>
      </MDBox>
    </Card >
  );
}

export default BookCard;
