import Button from '@material-ui/core/Button'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Grid from '@material-ui/core/Grid'
import React from 'react'
import Typography from '@material-ui/core/Typography'
import { Form, Formik } from 'formik'

class InventoryDeleteModal extends React.Component {
  render() {
    const {
      handleDialog,
      handleDelete,
      isDialogOpen,
      initialValues,
    } = this.props
    //console.log("The initial value is: " + initialValues + " type is: " + typeof(initialValues))
    return (
      <Dialog
        open={isDialogOpen}
        onClose={() => { handleDialog(false) }}
      >
        <Formik
          initialValues={initialValues}
          onSubmit={values => {
            // console.log("inside the inventory delete form ///////////////////////")
            // console.log(" the values are: " + values + " and the type is: " + typeof(values))
            // console.log(values)

            values.forEach(id => {
              console.log("The id is: " + id)
              handleDelete(id)
            })
            handleDialog(true)
          }}>
          {helpers =>
            <Form autoComplete='off' id={'deleteInventory'}>
              <DialogTitle id='alert-dialog-title'>Delete Inventory</DialogTitle>
              <DialogContent>
                <Grid container>
                  <Grid item xs={12}>
                    <Typography>
                      Are you sure you want to delete this inventory?
                    </Typography>
                  </Grid>
                </Grid>
              </DialogContent>
              <DialogActions>
                <Button onClick={() => { handleDialog(false) }} color='secondary'>No</Button>
                <Button disableElevation variant='contained' type='submit' form='deleteInventory' color='secondary'>
                  Yes
                </Button>
              </DialogActions>
            </Form>
          }
        </Formik>
      </Dialog>
    )
  }
}

InventoryDeleteModal.defaultProps = {
  delete: {}
}

export default InventoryDeleteModal