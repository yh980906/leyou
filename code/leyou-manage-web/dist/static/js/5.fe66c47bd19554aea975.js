webpackJsonp([5, 9], {
  RPpC: function (t, e, a) {
    "use strict";
    Object.defineProperty(e, "__esModule", {value: !0});
    var n = a("QmSG"), i = {
      name: "brand-form",
      props: {oldBrand: Object, isEdit: {type: Boolean, default: !1}, show: {type: Boolean, default: !0}},
      data: function () {
        return {
          baseUrl: n.a.api,
          valid: !1,
          brand: {name: "", image: "", letter: "", categories: []},
          imageDialogVisible: !1
        }
      },
      watch: {
        oldBrand: {
          deep: !0, handler: function (t) {
            Object.deepCopy(t, this.brand)
          }
        }
      },
      methods: {
        submit: function () {
          var t = this;
          this.$refs.brandForm.validate() && (this.brand.categories = this.brand.categories.map(function (t) {
            return t.id
          }), this.brand.letter = this.brand.letter.toUpperCase(), this.$http({
            method: this.isEdit ? "put" : "post",
            url: "/item/brand",
            data: this.$qs.stringify(this.brand)
          }).then(function () {
            t.$message.success("保存成功！"), t.closeWindow()
          }).catch(function () {
            t.$message.error("保存失败！")
          }))
        }, clear: function () {
          this.$refs.brandForm.reset(), this.categories = []
        }, handleImageSuccess: function (t) {
          this.brand.image = t
        }, removeImage: function () {
          this.brand.image = ""
        }, closeWindow: function () {
          this.$emit("close")
        }
      }
    }, s = {
      render: function () {
        var t = this, e = t.$createElement, a = t._self._c || e;
        return a("v-form", {
          ref: "brandForm", model: {
            value: t.valid, callback: function (e) {
              t.valid = e
            }, expression: "valid"
          }
        }, [a("v-text-field", {
          attrs: {
            label: "品牌名称", rules: [function (t) {
              return !!t || "品牌名称不能为空"
            }], counter: 10, required: ""
          }, model: {
            value: t.brand.name, callback: function (e) {
              t.$set(t.brand, "name", e)
            }, expression: "brand.name"
          }
        }), t._v(" "), a("v-text-field", {
          attrs: {
            label: "首字母", rules: [function (t) {
              return 1 === t.length || "首字母只能是1位"
            }], required: "", mask: "A"
          }, model: {
            value: t.brand.letter, callback: function (e) {
              t.$set(t.brand, "letter", e)
            }, expression: "brand.letter"
          }
        }), t._v(" "), a("v-cascader", {
          attrs: {url: "/item/category/list", required: "", multiple: "", label: "商品分类"},
          model: {
            value: t.brand.categories, callback: function (e) {
              t.$set(t.brand, "categories", e)
            }, expression: "brand.categories"
          }
        }), t._v(" "), a("v-layout", {attrs: {row: ""}}, [a("v-flex", {attrs: {xs3: ""}}, [a("span", {
          staticStyle: {
            "font-size": "16px",
            color: "#444"
          }
        }, [t._v("品牌LOGO：")])]), t._v(" "), a("v-flex", [a("v-upload", {
          attrs: {
            url: "/item/upload",
            multiple: !1,
            "pic-width": 250,
            "pic-height": 90
          }, model: {
            value: t.brand.image, callback: function (e) {
              t.$set(t.brand, "image", e)
            }, expression: "brand.image"
          }
        })], 1)], 1), t._v(" "), a("v-layout", {staticClass: "my-4"}, [a("v-btn", {
          attrs: {color: "primary"},
          on: {click: t.submit}
        }, [t._v("提交")]), t._v(" "), a("v-btn", {
          attrs: {color: "warning"},
          on: {click: t.clear}
        }, [t._v("重置")])], 1)], 1)
      }, staticRenderFns: []
    };
    var r = a("VU/8")(i, s, !1, function (t) {
      a("vchh")
    }, "data-v-166e50c5", null);
    e.default = r.exports
  }, h8yd: function (t, e) {
  }, ssss: function (t, e, a) {
    "use strict";
    Object.defineProperty(e, "__esModule", {value: !0});
    var n = a("RPpC"), i = a("MJ8A"), s = {
      name: "brand", components: {BrandForm: n.default}, data: function () {
        return {
          search: "",
          totalItems: 0,
          items: [],
          loading: !0,
          pagination: {},
          headers: [{text: "id", align: "center", value: "id"}, {
            text: "名称",
            align: "center",
            sortable: !1,
            value: "name"
          }, {text: "LOGO", align: "center", sortable: !1, value: "image"}, {
            text: "首字母",
            align: "center",
            value: "letter",
            sortable: !0
          }, {text: "操作", align: "center", value: "id", sortable: !1}],
          show: !1,
          brand: {},
          isEdit: !1
        }
      }, watch: {
        pagination: {
          handler: function () {
            this.getDataFromApi()
          }, deep: !0
        }, search: {
          handler: function () {
            this.getDataFromApi()
          }
        }, show: function (t, e) {
          e && this.getDataFromApi()
        }
      }, mounted: function () {
        this.getDataFromApi()
      }, methods: {
        addBrand: function () {
          this.brand = {}, this.isEdit = !1, this.show = !0
        }, editBrand: function (t) {
          var e = this;
          this.brand = t, this.isEdit = !0, this.show = !0, this.$http.get("/item/category/bid/" + t.id).then(function (t) {
            e.brand.categories = t.data
          })
        }, deleteBrand: function (t) {
          var e = this;
          this.$message.confirm("此操作将永久删除该品牌, 是否继续?").then(function () {
            e.$http.delete("/item/brand?id=" + t.id).then(function () {
              e.$message.success("删除成功！"), e.getDataFromApi()
            })
          }).catch(function () {
            e.$message.info("删除已取消！")
          })
        }, getDataFromApi: function () {
          var t = this;
          this.loading = !0, window.setTimeout(function () {
            t.items = i.a.slice(0, 4), t.totalItems = 100, t.loading = !1
          }, 200)
        }
      }
    }, r = {
      render: function () {
        var t = this, e = t.$createElement, a = t._self._c || e;
        return a("v-card", [a("v-card-title", [a("v-btn", {
          attrs: {color: "primary"},
          on: {click: t.addBrand}
        }, [t._v("新增品牌")]), t._v(" "), a("v-spacer"), t._v(" "), a("v-text-field", {
          attrs: {
            "append-icon": "search",
            label: "搜索",
            "single-line": "",
            "hide-details": ""
          }, model: {
            value: t.search, callback: function (e) {
              t.search = e
            }, expression: "search"
          }
        })], 1), t._v(" "), a("v-divider"), t._v(" "), a("v-data-table", {
          staticClass: "elevation-1",
          attrs: {
            headers: t.headers,
            items: t.items,
            pagination: t.pagination,
            "total-items": t.totalItems,
            loading: t.loading
          },
          on: {
            "update:pagination": function (e) {
              t.pagination = e
            }
          },
          scopedSlots: t._u([{
            key: "items", fn: function (e) {
              return [a("td", {staticClass: "text-xs-center"}, [t._v(t._s(e.item.id))]), t._v(" "), a("td", {staticClass: "text-xs-center"}, [t._v(t._s(e.item.name))]), t._v(" "), a("td", {staticClass: "text-xs-center"}, [e.item.image ? a("img", {
                attrs: {
                  width: "102",
                  height: "36",
                  src: e.item.image
                }
              }) : t._e()]), t._v(" "), a("td", {staticClass: "text-xs-center"}, [t._v(t._s(e.item.letter))]), t._v(" "), a("td", {staticClass: "justify-center layout px-0"}, [a("v-btn", {
                attrs: {icon: ""},
                on: {
                  click: function (a) {
                    t.editBrand(e.item)
                  }
                }
              }, [a("i", {staticClass: "el-icon-edit"})]), t._v(" "), a("v-btn", {
                attrs: {icon: ""},
                on: {
                  click: function (a) {
                    t.deleteBrand(e.item)
                  }
                }
              }, [a("i", {staticClass: "el-icon-delete"})])], 1)]
            }
          }, {
            key: "expand", fn: function (e) {
              return [a("v-card", {attrs: {flat: ""}}, [a("v-card-text", [t._v("Peek-a-boo!")])], 1)]
            }
          }, {
            key: "pageText", fn: function (e) {
              return [t._v("\n      共" + t._s(e.itemsLength) + "条,当前:" + t._s(e.pageStart) + " - " + t._s(e.pageStop) + "\n    ")]
            }
          }])
        }, [a("template", {slot: "no-data"}, [a("v-alert", {
          attrs: {
            value: !0,
            color: "error",
            icon: "warning"
          }
        }, [t._v("\n        对不起，没有查询到任何数据 :(\n      ")])], 1)], 2), t._v(" "), t.show ? a("v-dialog", {
          attrs: {
            "max-width": "600",
            scrollable: ""
          }, model: {
            value: t.show, callback: function (e) {
              t.show = e
            }, expression: "show"
          }
        }, [a("v-card", [a("v-toolbar", {
          attrs: {
            dark: "",
            dense: "",
            color: "primary"
          }
        }, [a("v-toolbar-title", [t._v(t._s(t.isEdit ? "修改品牌" : "新增品牌"))]), t._v(" "), a("v-spacer"), t._v(" "), a("v-btn", {
          attrs: {icon: ""},
          on: {
            click: function (e) {
              t.show = !1
            }
          }
        }, [a("v-icon", [t._v("close")])], 1)], 1), t._v(" "), a("v-card-text", {staticClass: "px-5 py-2"}, [a("brand-form", {
          attrs: {
            oldBrand: t.brand,
            isEdit: t.isEdit,
            reload: t.getDataFromApi
          }, on: {
            close: function (e) {
              t.show = !1
            }
          }
        })], 1)], 1)], 1) : t._e()], 1)
      }, staticRenderFns: []
    };
    var o = a("VU/8")(s, r, !1, function (t) {
      a("h8yd")
    }, "data-v-28ccdff5", null);
    e.default = o.exports
  }, vchh: function (t, e) {
  }
});
//# sourceMappingURL=5.fe66c47bd19554aea975.js.map
