import Button from '@material-ui/core/Button'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Grid from '@material-ui/core/Grid'
import React from 'react'
import TextField from '../Form/TextField'
import { Field, Form, Formik } from 'formik'
import { Checkbox, FormControlLabel, MenuItem } from '@material-ui/core'
import moment from 'moment'


class InventoryFormModal extends React.Component {
  render() {
    const {
      formName,
      handleDialog,
      handleInventory,
      title,
      initialValues,
      isDialogOpen,
      unitOfMeasurement,
      productType,
      selected
    } = this.props

    return (
      <Dialog
        open={isDialogOpen}
        maxWidth='sm'
        fullWidth={true}
        onClose={() => { handleDialog(false) }}
      >
        <Formik
          initialValues={initialValues}
          onSubmit={values => {
            //console.log("You are in "+ title + " form. The values is:" + JSON.stringify(values))
            values.bestBeforeDate = moment(values.bestBeforeDate).format("YYYY-MM-DDTHH:mm:ss.sss") + "Z"
            if(title === "Create"){
              handleInventory(values)
            }
            if(title === "Edit"){
              handleInventory(selected,values)
            }
            handleDialog(true)
          }}
          validate={values => {
            let errors = {}
            if (!values.name) {
              errors.name = "Please fill out the name field"
            }
            if (!values.productType) {
              errors.productType = "Please select a product type"
            }
            if (!values.unitOfMeasurement) {
              errors.unitOfMeasurement = "Please select an unit of measurement"
            }
            if (values.amount < 0) {
              errors.amount = "Amount cannot be negative"
            }
            if (values.averagePrice < 0) {
              errors.averagePrice = "Average price cannot be negative"
            }
            return errors
          }}
        >
          {helpers =>
            <Form
              autoComplete='off'
              id={formName}
            >
              <DialogTitle id='alert-dialog-title'>
                {`${title} Inventory`}
              </DialogTitle>
              <DialogContent>
                <Grid container spacing={2}>
                  {/* Name */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='name'
                      label='Name'
                      required
                      component={TextField}
                    />
                  </Grid>

                  {/* Product Type */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='productType'
                      label='Product Type'
                      select
                      required
                      component={TextField}
                    >
                      {productType.map(product => {
                        return (
                          <MenuItem key={product.id} value={product.name}>
                            {product.name}
                          </MenuItem>
                        )
                      })}
                    </Field>
                  </Grid>

                  {/* Description */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='description'
                      label='Description'
                      component={TextField}
                    />
                  </Grid>

                  {/* Average Price */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='averagePrice'
                      label='Average Price'
                      type="number"
                      component={TextField}
                    />
                  </Grid>

                  {/* Amount */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='amount'
                      label='Amount'
                      type="number"
                      component={TextField}
                    />
                  </Grid>

                  {/* UOM: Required Dropdown of constants from backend*/}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='unitOfMeasurement'
                      label='Unit of Measurement'
                      select
                      defaultValue=""
                      required
                      component={TextField}
                    >
                      {Object.keys(unitOfMeasurement).map((key) => {
                        return (
                          <MenuItem key={unitOfMeasurement[key].abbreviation} value={key}>
                            {unitOfMeasurement[key].name}
                          </MenuItem>
                        )
                      })}
                    </Field>
                  </Grid>

                  {/* BBD */}
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='bestBeforeDate'
                      label='Best Before Date'
                      type="date"
                      value={initialValues.bestBeforeDate}
                      InputLabelProps={{ shrink: true }}
                      component={TextField}
                    />
                  </Grid>

                  {/* Never Expires*/}
                  <Grid item xs={12} sm={12} >
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      as={FormControlLabel}
                      name='neverExpires'
                      label='Never Expires'
                      control={<Checkbox />}
                      type="checkbox"
                    />
                  </Grid>
                </Grid>

              </DialogContent>
              <DialogActions>
                <Button onClick={() => { handleDialog(false) }} color='secondary'>Cancel</Button>
                <Button
                  disableElevation
                  variant='contained'
                  type='submit'
                  form={formName}
                  color='secondary'
                  disabled={!helpers.dirty}>
                  Save
                </Button>
              </DialogActions>
            </Form>
          }
        </Formik>
      </Dialog>
    )
  }
}

export default InventoryFormModal