import axios from 'axios'
import { createAction, handleActions } from 'redux-actions'

const actions = {
  INVENTORY_GET_ALL: 'inventory/get_all',
  INVENTORY_GET_ALL_PENDING: 'inventory/get_all_PENDING',
  INVENTORY_UPDATE_PENDING: 'inventory/update_PENDING',
  INVENTORY_SAVE_PENDING: 'inventory/save_PENDING',
  INVENTORY_DELETE_PENDING: 'inventory/delete_PENDING',
  INVENTORY_UPDATE: 'inventory/update',
  INVENTORY_SAVE: 'inventory/save',
  INVENTORY_DELETE: 'inventory/delete',
  INVENTORY_REFRESH: 'inventory/refresh'
}

export let defaultState = {
  all: []
}

//GET
export const findInventory = createAction(actions.INVENTORY_GET_ALL, () => {
  return (dispatch, getState, config) => axios
    .get(`${config.restAPIUrl}/inventory`)
    .then((suc) => dispatch(refreshInventory(suc.data)))
})

//POST
export const saveInventory = createAction(actions.INVENTORY_SAVE, (inventory) =>
  async (dispatch, getState, config) => await axios
    .post(`${config.restAPIUrl}/inventory`, inventory)
    .then((suc) => {
      const invs = []
      getState().inventory.all.forEach(inv => {
        if (inv.id !== suc.data.id) {
          invs.push(inv)
        }
      })
      invs.push(suc.data)
      dispatch(refreshInventory(invs))
    })
)

//delete
export const removeInventory = createAction(actions.INVENTORY_DELETE, (ids) =>
  (dispatch, getState, config) => axios
    .delete(`${config.restAPIUrl}/inventory`, { data: ids })
    .then((suc) => {
      const invs = []
      getState().inventory.all.forEach(inv => {
        if (!ids.includes(inv.id)) {
          invs.push(inv)
        }
      })
      dispatch(refreshInventory(invs))
    })
)

//update
export const updateInventory = createAction(actions.INVENTORY_UPDATE, (ids, inventory) =>
  async (dispatch, getState, config) => await axios
    .put(`${config.restAPIUrl}/inventory/${ids}`, inventory)
    .then((suc) => {
      const invs = []
      getState().inventory.all.forEach(inv => {
        if (inventory.id !== inv.id) {
          invs.push(inv)
        } else {
          inv = { ...inventory };
          invs.push(inv)
        }
      })
      dispatch(refreshInventory(invs))
    })
)


//REFRESH
export const refreshInventory = createAction(actions.INVENTORY_REFRESH, (payload) =>
  payload.sort((inventoryA, inventoryB) => inventoryA.name < inventoryB.name ? -1 : inventoryA.name > inventoryB.name ? 1 : 0)
)

export default handleActions({
  //TODO
  [actions.INVENTORY_GET_ALL_PENDING]: (state) => ({
    ...state
  }),
  [actions.INVENTORY_REFRESH]: (state, action) => ({
    ...state,
    all: action.payload,
  }),
  [actions.INVENTORY_SAVE]: (state, action) =>
  ({
    ...state,
    all: [...state.all],
  }),
  [actions.INVENTORY_DELETE]: (state, action) => ({
    ...state,
    all: state.all.filter(inv => inv.id !== action.payload.ids),
  }),
  [actions.INVENTORY_UPDATE]: (state, action) => ({
    ...state,
    all: state.all.map((inv) => {
      if (inv.id === action.payload.ids) {
        return {
          ...inv,
          ...action.payload.inventory
        };
      } else {
        return inv;
      }
    }),
  }),
}, defaultState)
